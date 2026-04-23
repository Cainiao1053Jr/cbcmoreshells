package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.combat_command;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedDualCannonContraption;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
import com.cainiao1053.cbcmoreshells.index.CBCMSDualCannonMaterials;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import rbasamoyai.createbigcannons.utils.CBCUtils;

import java.util.Comparator;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;

public class CombatCommandInfoItem extends Item {
	private DualCannonMaterial recordedMaterial;

	public CombatCommandInfoItem(Properties properties) {
		super(properties);
	}

	Logger LOGGER = Cbcmoreshells.LOGGER;

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide) {
			CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
			CompoundTag tag = cd != null ? cd.copyTag() : new CompoundTag();

			if (!player.isShiftKeyDown()) {
				int cannonCount = 0;
				int maxCooldown = 0;
				int coolingCount = 0;
				int activationCount = 0;
				int totalCooldown = 0;
				int cannonReady = 0;
				int totalActivationLeft = 0;

				List<MountedDualCannonContraption> dualCannons = findCannons(level, player.position(), 48);
				this.recordedMaterial = DualCannonMaterial.fromNameOrNull(CBCUtils.location(tag.getString("Material")));
				if (this.recordedMaterial == null) {
					this.recordedMaterial = CBCMSDualCannonMaterials.CAST_IRON;
					tag.putString("Material", recordedMaterial.name().toString());
					stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
				}
				for (MountedDualCannonContraption dualCannon : dualCannons) {
					if (dualCannon.getCannonMaterial() != recordedMaterial) continue;
					cannonCount++;
					if (dualCannon.getCommandActivation()) {
						activationCount++;
						totalActivationLeft += dualCannon.getCommandLeft();
						continue;
					}
					int cooldown = dualCannon.getCommandCooldown();
					if (cooldown > 0) {
						coolingCount++;
						totalCooldown += cooldown;
						if (cooldown > maxCooldown) maxCooldown = cooldown;
					} else {
						if (!dualCannon.getCommandActivation()) cannonReady++;
					}
				}
				float averageCooldown = coolingCount != 0 ? (float) totalCooldown / (20 * coolingCount) : 0;
				float averageActivationLeft = activationCount != 0 ? (float) totalActivationLeft / (20 * activationCount) : 0;
				Component msg = Component.translatable("item.cbcmoreshells.combat_command_base.info",
						cannonCount, cannonReady, activationCount,
						averageActivationLeft, coolingCount,
						String.format("%.1f", averageCooldown),
						String.format("%.1f", (float) maxCooldown / 20));
				player.sendSystemMessage(msg);
			} else {
				MountedDualCannonContraption cannon = findNearestCannon(level, player.position(), 32);
				if (cannon == null) {
					player.getCooldowns().addCooldown(this, 30);
					player.sendSystemMessage(Component.translatable("item.cbcmoreshells.combat_command_base.change_material_fail"));
					return super.use(level, player, hand);
				}
				this.recordedMaterial = cannon.getCannonMaterial();
				tag.putString("Material", recordedMaterial.name().toString());
				stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
				Component matName = Component.translatable(
						"block.cbcmoreshells.material." + recordedMaterial.name().getPath());
				player.sendSystemMessage(Component.translatable("item.cbcmoreshells.combat_command_base.change_material", matName));
			}
		}
		player.getCooldowns().addCooldown(this, 40);
		return super.use(level, player, hand);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		return super.useOn(context);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		boolean desc = Screen.hasShiftDown();
		if (!desc) {
			addHoldShift(desc, tooltip);
			return;
		}
		CBCMSTooltip.appendCombatCommandMainInfo(stack, context, tooltip, flag, 0);
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
