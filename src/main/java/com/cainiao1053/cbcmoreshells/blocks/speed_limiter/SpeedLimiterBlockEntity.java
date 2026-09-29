package com.cainiao1053.cbcmoreshells.blocks.speed_limiter;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.lang.LangNumberFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SpeedLimiterBlockEntity extends SplitShaftBlockEntity {

	public static final float MIN_RATIO = 1 / 256f;
	private static final int CHECK_INTERVAL = 20;

	protected float speedRatio = 1;
	private int checkTimer = CHECK_INTERVAL;

	public SpeedLimiterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		super.tick();
		if (level.isClientSide)
			return;
		if (--checkTimer > 0)
			return;
		checkTimer = CHECK_INTERVAL;
		limitSpeedIfOverStressed();
	}

	protected void limitSpeedIfOverStressed() {
		if (!overStressed || !hasSource() || stress <= 0)
			return;
		float inputSpeed = Math.abs(getTheoreticalSpeed());
		if (inputSpeed == 0)
			return;

		// Stress scales linearly with speed, so scaling the output down by capacity / stress
		// removes the excess if all of it comes from the output side. Any remaining overload
		// (e.g. from input-side consumers) is handled by the next check.
		float targetOutputSpeed = inputSpeed * speedRatio * capacity / stress;
		if (targetOutputSpeed >= 1)
			targetOutputSpeed = Mth.floor(targetOutputSpeed);
		float newRatio = Mth.clamp(targetOutputSpeed / inputSpeed, MIN_RATIO, 1);
		if (newRatio >= speedRatio)
			return;
		setPowered(true);
		setSpeedRatio(newRatio);
	}

	protected void setPowered(boolean powered) {
		BlockState state = getBlockState();
		if (!state.hasProperty(SpeedLimiterBlock.POWERED) || state.getValue(SpeedLimiterBlock.POWERED) == powered)
			return;
		level.setBlock(worldPosition, state.setValue(SpeedLimiterBlock.POWERED, powered), Block.UPDATE_ALL);
	}

	public float getSpeedRatio() {
		return speedRatio;
	}

	public void setSpeedRatio(float ratio) {
		if (speedRatio == ratio)
			return;
		speedRatio = ratio;
		if (level != null && !level.isClientSide && hasSource()) {
			// Re-propagate the output side with the new modifier
			detachKinetics();
			attachKinetics();
		}
		setChanged();
		sendData();
	}

	public void resetSpeedRatio() {
		setPowered(false);
		setSpeedRatio(1);
	}

	@Override
	public float getRotationSpeedModifier(Direction face) {
		if (isVirtual())
			return 1;
		return hasSource() && face != getSourceFacing() ? speedRatio : 1;
	}

	public float getOutputSpeed() {
		return getSpeed() * speedRatio;
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		super.addToGoggleTooltip(tooltip, isPlayerSneaking);

		lang().translate("speed_limiter.goggles.title").forGoggles(tooltip);
		lang().translate("speed_limiter.goggles.speed_ratio").style(ChatFormatting.GRAY).forGoggles(tooltip);
		lang()
			.text(LangNumberFormat.format(speedRatio))
			.style(speedRatio < 1 ? ChatFormatting.GOLD : ChatFormatting.AQUA)
			.forGoggles(tooltip, 1);

		lang().translate("speed_limiter.goggles.output_speed").style(ChatFormatting.GRAY).forGoggles(tooltip);
		CreateLang.builder()
			.text(LangNumberFormat.format(Math.abs(getOutputSpeed())))
			.space()
			.translate("generic.unit.rpm")
			.style(ChatFormatting.AQUA)
			.forGoggles(tooltip, 1);
		return true;
	}

	private static LangBuilder lang() {
		return new LangBuilder(Cbcmoreshells.MODID);
	}

	@Override
	public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		compound.putFloat("SpeedRatio", speedRatio);
		super.write(compound, registries, clientPacket);
	}

	@Override
	protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		speedRatio = compound.contains("SpeedRatio") ? Mth.clamp(compound.getFloat("SpeedRatio"), MIN_RATIO, 1) : 1;
		super.read(compound, registries, clientPacket);
	}

}
