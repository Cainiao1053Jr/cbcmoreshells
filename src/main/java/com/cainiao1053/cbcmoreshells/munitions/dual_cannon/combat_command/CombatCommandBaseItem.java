package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.combat_command;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedDualCannonContraption;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;

import java.util.Comparator;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;

public class CombatCommandBaseItem extends Item {
	private DualCannonMaterial recordedMaterial;

	public CombatCommandBaseItem(Properties properties) {
		super(properties);
	}

	Logger LOGGER = Cbcmoreshells.LOGGER;

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> {
				CompoundTag nbt = data.copyTag();
				nbt.remove("Positions");
				return CustomData.of(nbt);
			});
			if (!level.isClientSide()) {
				player.sendSystemMessage(Component.translatable(
						"item.cbcmoreshells.combat_command_base.positions_cleared"));
			}
		}
		player.getCooldowns().addCooldown(this, 50);
		return super.use(level, player, hand);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();
		if (player == null) {
			return InteractionResult.PASS;
		}
		if (!player.isShiftKeyDown()) {
			BlockPos pos = context.getClickedPos();
			BlockEntity be = context.getLevel().getBlockEntity(pos);
			if (be instanceof CannonMountBlockEntity) {
				CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
				CompoundTag tag = cd != null ? cd.copyTag() : new CompoundTag();
				ListTag list = tag.contains("Positions", Tag.TAG_LIST)
						? tag.getList("Positions", Tag.TAG_INT_ARRAY)
						: new ListTag();
				boolean alreadyAdded = false;
				for (int i = 0; i < list.size(); i++) {
					BlockPos storedPos = BlockPos.CODEC.parse(NbtOps.INSTANCE, list.get(i))
							.result()
							.orElse(null);

					if (pos.equals(storedPos)) {
						alreadyAdded = true;
						break;
					}
				}
				if (!alreadyAdded) {
					list.add(NbtUtils.writeBlockPos(pos));
					tag.put("Positions", list);
					stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
					if (!context.getLevel().isClientSide()) {
						player.sendSystemMessage(Component.translatable(
								"item.cbcmoreshells.combat_command_base.add_position", pos.getX(), pos.getY(), pos.getZ()));
					}
				} else {
					if (!context.getLevel().isClientSide()) {
						player.sendSystemMessage(Component.translatable(
								"item.cbcmoreshells.combat_command_base.position_already_added"));
					}
				}
				return InteractionResult.SUCCESS;
			}
		} else {
			stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> {
				CompoundTag nbt = data.copyTag();
				nbt.remove("Positions");
				return CustomData.of(nbt);
			});
			if (!context.getLevel().isClientSide()) {
				player.sendSystemMessage(Component.translatable(
						"item.cbcmoreshells.combat_command_base.positions_cleared"));
			}
			return InteractionResult.SUCCESS;
		}
		return super.useOn(context);
	}

	public float getCommandDurabilityModifier() { return 1; }
	public float getCommandReloadTimeModifier() { return 1; }
	public float getCommandSpreadModifier() { return 1f; }
	public float getCommandLifetimeModifier() { return 1; }
	public int getMaximumUseAtOnce() { return 10; }

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		boolean desc = Screen.hasShiftDown();
		if (!desc) {
			addHoldShift(desc, tooltip);
			return;
		}
		CBCMSTooltip.appendCombatCommandMainInfo(stack, context, tooltip, flag, getMaximumUseAtOnce());
		CBCMSTooltip.appendCombatCommandMaterialInfo(stack, context, tooltip, flag);
	}

	public static List<MountedDualCannonContraption> findCannons(Level level, Vec3 center, double radius) {
		AABB box = AABB.ofSize(center, 2 * radius, 2 * radius, 2 * radius);

		return level.getEntitiesOfClass(OrientedContraptionEntity.class, box,
						e -> e.getContraption() instanceof MountedDualCannonContraption)
				.stream()
				.map(OrientedContraptionEntity::getContraption)
				.filter(c -> c instanceof MountedDualCannonContraption)
				.map(c -> (MountedDualCannonContraption) c)
				.distinct()
				.toList();
	}

	public static MountedDualCannonContraption findNearestCannon(Level level, Vec3 center, double radius) {
		AABB box = AABB.ofSize(center, 2 * radius, 2 * radius, 2 * radius);

		OrientedContraptionEntity nearest = level.getEntitiesOfClass(OrientedContraptionEntity.class, box,
						e -> e.getContraption() instanceof MountedDualCannonContraption)
				.stream()
				.min(Comparator.comparingDouble(e -> e.distanceToSqr(center)))
				.orElse(null);

		if (nearest == null) return null;
		return (nearest.getContraption() instanceof MountedDualCannonContraption m) ? m : null;
	}

}
