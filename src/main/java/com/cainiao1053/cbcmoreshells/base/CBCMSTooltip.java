package com.cainiao1053.cbcmoreshells.base;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.equipments.DualCannonChargerAttachment;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterialProperties;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.ReductiveTorpedoProperties;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.TorpedoProperties;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonIncendiaryProperties;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonProperties;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class CBCMSTooltip {

	private static Style primary = TooltipHelper.Palette.GRAY_AND_WHITE.primary();
	private static Style highlight = TooltipHelper.Palette.GRAY_AND_WHITE.highlight();

	public static void addHoldShift(boolean desc, List<Component> tooltip) {
		String[] holdDesc = Lang.translateDirect("tooltip.holdForDescription", "$").getString().split("\\$");

		Component keyShift = Lang.translateDirect("tooltip.keyShift");
		MutableComponent tabBuilder = Components.literal("");
		tabBuilder.append(Components.literal(holdDesc[0]).withStyle(ChatFormatting.DARK_GRAY));
		tabBuilder.append(keyShift.plainCopy().withStyle(ChatFormatting.GRAY));
		tabBuilder.append(Components.literal(holdDesc[1]).withStyle(ChatFormatting.DARK_GRAY));
		tooltip.add(tabBuilder);

	}

	public static <T extends Block & DualCannonBlock> void appendDualCannonBlockText(ItemStack stack, @Nullable Level level,
																				 List<Component> tooltip, TooltipFlag flag, T block) {
		boolean desc = Screen.hasShiftDown();
		addHoldShift(desc, tooltip);
		if (!desc) {
			return;
		}

		TooltipHelper.Palette palette = getPalette(level, stack);
		DualCannonMaterialProperties material = block.getCannonMaterial().properties();
		String rootKey = "block." + Cbcmoreshells.MODID + ".dual_cannon.tooltip";
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".info"),
				palette.primary(), palette.highlight(), 2));
		tooltip.add(Components.literal(I18n.get(rootKey + ".materialProperties")).withStyle(ChatFormatting.GRAY));

		tooltip.add(Components.literal(" " + I18n.get(rootKey + ".accuracy")).withStyle(ChatFormatting.GRAY));
		float spread = material.minimumSpread();
		float spreadReduction = material.spreadReductionPerBarrel();

		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".accuracy.info", spreadReduction, spread),
				palette.primary(), palette.highlight(), 2));

		tooltip.add(Components.literal(" " + I18n.get(rootKey + ".damageMultiplier")).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".damageMultiplier.info", material.durabilityMassModifier()),
				palette.primary(), palette.highlight(), 2));

		tooltip.add(Components.literal(" " + I18n.get(rootKey + ".addedLifetime")).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".addedLifetime.info", String.format("%.1f",(float) material.addedLifetime()/20)),
				palette.primary(), palette.highlight(), 2));

		tooltip.add(Components.literal(" " + I18n.get(rootKey + ".reloadTime")).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".reloadTime.info", String.format("%.1f",material.reloadTimeModifier()*2.5)),
				palette.primary(), palette.highlight(), 2));

		tooltip.add(Components.literal(" " + I18n.get(rootKey + ".combatCommand")).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".combatCommand.info", String.format("%.0f",(float)material.combatCommandDuration()/20),String.format("%.0f",(float)material.combatCommandCooldown()/20)),
				palette.primary(), palette.highlight(), 2));
	}

	public static <T extends Block & DualCannonBlock> void appendSingleCannonBlockText(ItemStack stack, @Nullable Level level,
																					 List<Component> tooltip, TooltipFlag flag, T block) {
		boolean desc = Screen.hasShiftDown();
		addHoldShift(desc, tooltip);
		if (!desc) {
			return;
		}

		TooltipHelper.Palette palette = getPalette(level, stack);
		DualCannonMaterialProperties material = block.getCannonMaterial().properties();
		String rootKey = "block." + Cbcmoreshells.MODID + ".dual_cannon.tooltip";
		String singleKey = "block." + Cbcmoreshells.MODID + ".single_cannon.tooltip";
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(singleKey + ".info"),
				palette.primary(), palette.highlight(), 2));
		tooltip.add(Components.literal(I18n.get(rootKey + ".materialProperties")).withStyle(ChatFormatting.GRAY));

		tooltip.add(Components.literal(" " + I18n.get(rootKey + ".accuracy")).withStyle(ChatFormatting.GRAY));
		float spread = material.minimumSpread();
		float spreadReduction = material.spreadReductionPerBarrel();

		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".accuracy.info", spreadReduction, spread),
				palette.primary(), palette.highlight(), 2));

		tooltip.add(Components.literal(" " + I18n.get(rootKey + ".damageMultiplier")).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".damageMultiplier.info", material.durabilityMassModifier()),
				palette.primary(), palette.highlight(), 2));

		tooltip.add(Components.literal(" " + I18n.get(rootKey + ".addedLifetime")).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".addedLifetime.info", String.format("%.1f",(float) material.addedLifetime()/20)),
				palette.primary(), palette.highlight(), 2));

		tooltip.add(Components.literal(" " + I18n.get(rootKey + ".reloadTime")).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".reloadTime.info", String.format("%.1f",material.reloadTimeModifier()*2.5)),
				palette.primary(), palette.highlight(), 2));

		tooltip.add(Components.literal(" " + I18n.get(rootKey + ".combatCommand")).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".combatCommand.info", String.format("%.0f",(float)material.combatCommandDuration()/20),String.format("%.0f",(float)material.combatCommandCooldown()/20)),
				palette.primary(), palette.highlight(), 2));
	}

	public static <T extends Block & DualCannonBlock> void appendDualCannonChargerBlockText(ItemStack stack, @Nullable Level level,
																							List<Component> tooltip, TooltipFlag flag, T block) {
		boolean desc = Screen.hasShiftDown();
		addHoldShift(desc, tooltip);
		if (!desc) {
			return;
		}

		TooltipHelper.Palette palette = getPalette(level, stack);
		String rootKey = "block." + Cbcmoreshells.MODID + ".dual_cannon.tooltip.charger";
		tooltip.add(Components.translatable(rootKey).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".info"),
				palette.primary(), palette.highlight(), 2));
	}

	public static void appendTorpedoInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
										 TooltipFlag flag, TorpedoProperties properties) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		float torpSpeed = properties.torpedoProperties().torpedoSpeed();
		float buoyancyFactor = properties.torpedoProperties().buoyancyFactor();
		int lifetime = properties.lifetime();
		int explosiveCooldown = properties.torpedoProperties().explosionCooldown();
		int reloadTime = properties.torpedoProperties().reloadTime();
		float resistanceFactor = properties.torpedoProperties().resistanceFactor();
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = "block.cbcmoreshells.torpedo" + ".tooltip.torpInfo";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main", String.format("%.1f",torpSpeed*38.8),
				String.format("%.1f", properties.explosion().explosivePower() * 4),
				buoyancyFactor,String.format("%.1f",lifetime*torpSpeed),String.format("%.1f",explosiveCooldown*torpSpeed), String.format("%.1f",((float)reloadTime /20)), String.format("%.2f", resistanceFactor)
		), palette.primary(), palette.highlight(), 1));
	}

	public static void appendReductiveTorpedoInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
										 TooltipFlag flag, ReductiveTorpedoProperties properties) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		float torpSpeed = properties.torpedoProperties().torpedoSpeed();
		float buoyancyFactor = properties.torpedoProperties().buoyancyFactor();
		int lifetime = properties.lifetime();
		int explosiveCooldown = properties.torpedoProperties().explosionCooldown();
		float reduction = properties.reductiveProjectileProperties().percentReduction();
		int reloadTime = properties.torpedoProperties().reloadTime();
		float resistanceFactor = properties.torpedoProperties().resistanceFactor();
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = "block.cbcmoreshells.reductive_torpedo" + ".tooltip.torpInfo";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main", String.format("%.1f",torpSpeed*38.8),
				String.format("%.1f", properties.explosion().explosivePower() * 4),
				buoyancyFactor,String.format("%.1f",lifetime*torpSpeed),String.format("%.1f",explosiveCooldown*torpSpeed),String.format("%.1f",((float)reloadTime /20)),
				String.format("%.1f",reduction * 100), String.format("%.2f", resistanceFactor)
		), palette.primary(), palette.highlight(), 1));
	}

	public static void appendBallisticInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
										 TooltipFlag flag, float durabilityMass, float penetration, float deflection) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = stack.getDescriptionId() + ".tooltip.ballisticInfo";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main", durabilityMass,penetration,String.format("%.0f",Math.acos(deflection)*180/Math.PI)), palette.primary(), palette.highlight(), 1));
	}

	public static void appendExplosiveInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
										   TooltipFlag flag, float durabilityMass, float penetration, float deflection, float explosion) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = stack.getDescriptionId() + ".tooltip.ballisticInfo";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main", durabilityMass,penetration,String.format("%.0f",Math.acos(deflection)*180/Math.PI),explosion), palette.primary(), palette.highlight(), 1));
	}

	public static void appendIncendiaryInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
										   TooltipFlag flag, float durabilityMass, float penetration, float deflection, float explosion, float fireChance, int fireRange) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = stack.getDescriptionId() + ".tooltip.ballisticInfo";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main", durabilityMass,penetration,String.format("%.0f",Math.acos(deflection)*180/Math.PI),explosion,String.format("%.0f",100*fireChance),fireRange), palette.primary(), palette.highlight(), 1));
	}

	public static void appendBombInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
											TooltipFlag flag, float durabilityMass, float penetration, float deflection, float explosion, float lifetime) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = stack.getDescriptionId() + ".tooltip.ballisticInfo";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main", durabilityMass,penetration,String.format("%.0f",Math.acos(deflection)*180/Math.PI),explosion,String.format("%.0f",lifetime/20)), palette.primary(), palette.highlight(), 1));
	}

	public static void appendRackedTorpedoInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
									  TooltipFlag flag, float explosion, float lifetime, float buoyancyFactor, float ssVel, float waterDamp) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = stack.getDescriptionId() + ".tooltip.ballisticInfo";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main",explosion,String.format("%.0f",lifetime/20), buoyancyFactor, String.format("%.1f",ssVel*38.8), waterDamp), palette.primary(), palette.highlight(), 1));
	}

	public static void appendRackedRocketInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
									  TooltipFlag flag, float durabilityMass, float penetration, float deflection, float explosion, float lifetime, float ssVel, float thrustTime) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = stack.getDescriptionId() + ".tooltip.ballisticInfo";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main", durabilityMass,penetration,String.format("%.0f",Math.acos(deflection)*180/Math.PI),explosion,String.format("%.0f",lifetime/20), String.format("%.0f",ssVel*20), String.format("%.0f",thrustTime/20)), palette.primary(), palette.highlight(), 1));
	}

	public static void appendTorpedoDetectorInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
											  TooltipFlag flag, float activationTime, float cooldown) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = stack.getDescriptionId() + ".tooltip";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main", String.format("%.0f",activationTime/20), String.format("%.0f",cooldown/20)), palette.primary(), palette.highlight(), 1));
	}

	public static void appendAmmoRackInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
												 TooltipFlag flag) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = "block.cbcmoreshells.ammo_rack.tooltip";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main"), palette.primary(), palette.highlight(), 1));
	}

	public static void appendDishPlateInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
										  TooltipFlag flag) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = "block.cbcmoreshells.dish_plate.tooltip";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main"), palette.primary(), palette.highlight(), 1));
	}

	public static void appendTorpedoTubeInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
										  TooltipFlag flag) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = "block.cbcmoreshells.torptube.tooltip";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main"), palette.primary(), palette.highlight(), 1));
	}

	public static void appendProxyFuzeInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
										  TooltipFlag flag) {
		boolean desc = Screen.hasShiftDown();
		addHoldShift(desc, tooltip);
		if (!desc) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = "item.cbcmoreshells.ship_proximity_fuze.tooltip";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main"), palette.primary(), palette.highlight(), 1));
	}

	public static void appendSensitiveFuzeInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
										   TooltipFlag flag) {
		boolean desc = Screen.hasShiftDown();
		addHoldShift(desc, tooltip);
		if (!desc) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = "item.cbcmoreshells.sensitive_impact_fuze.tooltip";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main"), palette.primary(), palette.highlight(), 1));
	}

	public static void appendLandingIndicatorInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
										  TooltipFlag flag) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = "block.cbcmoreshells.landing_indicator.tooltip";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main"), palette.primary(), palette.highlight(), 1));
	}

	public static void appendProjectileRackInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
											 TooltipFlag flag) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = "block.cbcmoreshells.projrack.tooltip";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main"), palette.primary(), palette.highlight(), 1));
	}

	public static void appendCombatCommandMaterialInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
												  TooltipFlag flag) {
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = "item.cbcmoreshells.combat_command.tooltip.material.main";
		CompoundTag tag = stack.getTag();

		if(tag != null && !tag.getString("Material").isEmpty()) {
			String m = tag.getString("Material");
			int i = m.indexOf(':');
			String path = (i >= 0) ? m.substring(i + 1)    : m;
			MutableComponent line = Component.empty().append(Components.translatable(key1).withStyle(ChatFormatting.GRAY))
					.append(Components.translatable("block.cbcmoreshells.material."+path).withStyle(palette.primary()));
			tooltip.add(line);
		}
	}

	public static void appendCombatCommandMainInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
													   TooltipFlag flag, int maxCannon) {
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = "item.cbcmoreshells.combat_command.tooltip.main";
		String key2 = stack.getDescriptionId();
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".desc"), palette.primary(), palette.highlight(), 1));
		tooltip.add(Components.translatable(key2).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key2 + ".desc", maxCannon), palette.primary(), palette.highlight(), 1));
	}

	public static void appendCombatCommandDamageInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
													   TooltipFlag flag, float durabilityMultiplier) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		String key1 = "item.cbcmoreshells.combat_command.tooltip.damage";
		float damageAddition = durabilityMultiplier-1;
		ChatFormatting color = switchColor(damageAddition,-0.5f,0.16f,0.36f,0.56f);
		if(damageAddition<0){
			tooltip.add(Components.translatable(key1+".negative",String.format("%.0f",-damageAddition*100)).withStyle(color));
		}else{
			tooltip.add(Components.translatable(key1,String.format("%.0f",damageAddition*100)).withStyle(color));
		}
	}

	public static void appendCombatCommandRangeInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
													 TooltipFlag flag, float durabilityMultiplier) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		String key1 = "item.cbcmoreshells.combat_command.tooltip.range";
		float damageAddition = durabilityMultiplier-1;
		ChatFormatting color = switchColor(damageAddition,-0.41f,0.21f,0.41f,0.61f);
		if(damageAddition<0){
			tooltip.add(Components.translatable(key1+".negative",String.format("%.0f",-damageAddition*100)).withStyle(color));
		}else{
			tooltip.add(Components.translatable(key1,String.format("%.0f",damageAddition*100)).withStyle(color));
		}
	}

	public static void appendCombatCommandSpreadInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
													 TooltipFlag flag, float spreadMultiplier) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		String key1 = "item.cbcmoreshells.combat_command.tooltip.spread";
		float spreadAddition = 1-spreadMultiplier;
		ChatFormatting color = switchColor(spreadAddition,-0.55f,0.21f,0.46f,0.71f);
		if(spreadAddition<0){
			tooltip.add(Components.translatable(key1+".negative",String.format("%.0f",-spreadAddition*100)).withStyle(color));
		}else{
			tooltip.add(Components.translatable(key1,String.format("%.0f",spreadAddition*100)).withStyle(color));
		}
	}

	public static void appendCombatCommandReloadInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
													 TooltipFlag flag, float spreadMultiplier) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		String key1 = "item.cbcmoreshells.combat_command.tooltip.reload";
		float spreadAddition = 1-spreadMultiplier;
		ChatFormatting color = switchColor(spreadAddition,-0.41f,0.31f,0.51f,0.71f);
		if(spreadAddition<0){
			tooltip.add(Components.translatable(key1+".negative",String.format("%.0f",-spreadAddition*100)).withStyle(color));
		}else{
			tooltip.add(Components.translatable(key1,String.format("%.0f",spreadAddition*100)).withStyle(color));
		}
	}

	public static void appendInertDualCannonProjectileInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
														   TooltipFlag flag, DualCannonProperties properties) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		float durabilityMass = properties.ballistics().durabilityMass();
		float initVel = properties.dualCannonProperties().initialVel();
		float projectileSpread = properties.dualCannonProperties().projectileSpread();
		float minimumSpread = properties.dualCannonProperties().minimumSpread();
		int lifetime = properties.dualCannonProperties().lifetime();
		float deflection = properties.ballistics().deflection();
		float smashToughness = properties.dualCannonProperties().smashToughness();
		float maximumMomentum = properties.dualCannonProperties().maximumMomentum();
		float reloadTimeCoef = properties.dualCannonProperties().reloadTimeCoef();
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = stack.getDescriptionId();
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main", durabilityMass, String.format("%.0f",initVel*20), projectileSpread, minimumSpread,
				String.format("%.1f",(float)lifetime/20),String.format("%.0f",Math.acos(deflection)*180/Math.PI), smashToughness,
				maximumMomentum, reloadTimeCoef), palette.primary(), palette.highlight(), 1));
	}

	public static void appendExplosiveDualCannonProjectileInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
														   TooltipFlag flag, DualCannonProperties properties) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		float durabilityMass = properties.ballistics().durabilityMass();
		float explosion = properties.explosion().explosivePower();
		float initVel = properties.dualCannonProperties().initialVel();
		float projectileSpread = properties.dualCannonProperties().projectileSpread();
		float minimumSpread = properties.dualCannonProperties().minimumSpread();
		int lifetime = properties.dualCannonProperties().lifetime();
		float deflection = properties.ballistics().deflection();
		float smashToughness = properties.dualCannonProperties().smashToughness();
		float maximumMomentum = properties.dualCannonProperties().maximumMomentum();
		float reloadTimeCoef = properties.dualCannonProperties().reloadTimeCoef();
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = stack.getDescriptionId();
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main", durabilityMass, explosion, String.format("%.0f",initVel*20), projectileSpread, minimumSpread,
				String.format("%.1f",(float)lifetime/20),String.format("%.0f",Math.acos(deflection)*180/Math.PI), smashToughness,
				maximumMomentum, reloadTimeCoef), palette.primary(), palette.highlight(), 1));
	}

	public static void appendIncendiaryDualCannonProjectileInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
															   TooltipFlag flag, DualCannonIncendiaryProperties properties) {
		if (!Screen.hasShiftDown()) {
			return;
		}
		float durabilityMass = properties.ballistics().durabilityMass();
		float explosion = properties.explosion().explosivePower();
		float initVel = properties.dualCannonProperties().initialVel();
		float projectileSpread = properties.dualCannonProperties().projectileSpread();
		float minimumSpread = properties.dualCannonProperties().minimumSpread();
		int lifetime = properties.dualCannonProperties().lifetime();
		float deflection = properties.ballistics().deflection();
		float smashToughness = properties.dualCannonProperties().smashToughness();
		float maximumMomentum = properties.dualCannonProperties().maximumMomentum();
		float fireChance = properties.incendiary().fireChance();
		int fireRange = properties.incendiary().fireRange();
		float reloadTimeCoef = properties.dualCannonProperties().reloadTimeCoef();
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = stack.getDescriptionId();
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main", durabilityMass, explosion,
				String.format("%.0f",fireChance*100), fireRange,
				String.format("%.0f",initVel*20), projectileSpread, minimumSpread,
				String.format("%.1f",(float)lifetime/20),String.format("%.0f",Math.acos(deflection)*180/Math.PI), smashToughness,
				maximumMomentum, reloadTimeCoef), palette.primary(), palette.highlight(), 1));
	}

	public static void genericItemTooltipInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip,
												   TooltipFlag flag) {
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key1 = stack.getDescriptionId() + ".tooltip";
		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".main"), palette.primary(), palette.highlight(), 1));
	}

	public static void appendCannonComboInfo(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		appendCombatCommandMaterialInfo(stack, level, tooltip, flag);
		//do something
	}



	private static Component getNoGogglesMeter(int outOfFive, boolean invertColor, boolean canBeInvalid) {
		int value = invertColor ? 5 - outOfFive : outOfFive;
		ChatFormatting color = switch (value) {
			case 0, 1 -> ChatFormatting.RED;
			case 2, 3 -> ChatFormatting.GOLD;
			case 4, 5 -> ChatFormatting.YELLOW;
			default -> canBeInvalid ? ChatFormatting.DARK_GRAY : value < 0 ? ChatFormatting.RED : ChatFormatting.YELLOW;
		};
		return Components.literal(" " + TooltipHelper.makeProgressBar(5, outOfFive)).withStyle(color);
	}

	public static TooltipHelper.Palette getPalette(Level level, ItemStack stack) {
		return TooltipHelper.Palette.STANDARD_CREATE;
	}

	public static ChatFormatting switchColor(float value, float worst, float normal, float good, float epic) {
		if(value<worst){
			return ChatFormatting.DARK_RED;
		}else if(value<0){
			return ChatFormatting.RED;
		}else if(value<normal){
			return ChatFormatting.GREEN;
		}else if(value<good){
			return ChatFormatting.AQUA;
		}else if(value<epic){
			return ChatFormatting.DARK_PURPLE;
		}else {
			return ChatFormatting.GOLD;
		}
	}

}
