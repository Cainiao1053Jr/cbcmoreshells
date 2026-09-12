package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.verr1.shaolib.api.projectile.ProjectileType;
import com.verr1.shaolib.munitions.config.MunitionConfigManager;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyType;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class CBCMSDualCannonMunitionRegistry {

	private static volatile Map<Block, Entry> byBlock;

	private CBCMSDualCannonMunitionRegistry() {}
	public record Entry(
		ProjectileType<DualCannonState> projectileType,
		MunitionPropertyType<? extends DualCannonMunitionProperties> propertyType,
		DualCannonBehavior.Kind kind,
		CBCMSDualCannonLaunchProfile launchProfile,
		boolean fuzed,
		boolean baseFuze,
		ResourceLocation renderedBlock
	) {}

	@Nullable
	public static Entry of(Block block) {
		return block == null ? null : map().get(block);
	}

	@Nullable
	public static Entry of(BlockState state) {
		return state == null ? null : of(state.getBlock());
	}

	@Nullable
	public static Entry of(ItemStack stack) {
		if (stack == null || stack.isEmpty()) return null;
		return stack.getItem() instanceof BlockItem blockItem ? of(blockItem.getBlock()) : null;
	}

	public static Collection<Entry> all() {
		return Collections.unmodifiableCollection(map().values());
	}

	/**
	 * Every registered projectile block, in registration order. An {@link Entry} does not point back
	 * at its block, so this is the way in for anything that needs the block's item — firing tables
	 * hang their per-shell hooks off {@code FuzedDualCannonProjectileBlockItem}.
	 */
	public static Collection<Block> blocks() {
		return Collections.unmodifiableCollection(map().keySet());
	}
	public static DualCannonMunitionProperties properties(Entry entry) {
		return resolve(entry.propertyType());
	}

	public static DualCannonLaunchProperties launchProperties(Entry entry) {
		return properties(entry).dualCannon();
	}

	public static float reloadTimeCoefficient(ItemStack stack) {
		Entry entry = of(stack);
		return entry == null ? 1.0F : (float) launchProperties(entry).reloadTimeCoefficient();
	}

	private static <P extends DualCannonMunitionProperties> P resolve(MunitionPropertyType<P> type) {
		return MunitionConfigManager.properties(type);
	}

	private static Map<Block, Entry> map() {
		Map<Block, Entry> local = byBlock;
		if (local == null) {
			synchronized (CBCMSDualCannonMunitionRegistry.class) {
				local = byBlock;
				if (local == null) {
					local = build();
					byBlock = local;
				}
			}
		}
		return local;
	}

	private static Map<Block, Entry> build() {
		Map<Block, Entry> entries = new LinkedHashMap<>();

		// Inert shot: no burst charge, so no fuze socket either.
		put(entries, CBCMSBlocks.NORMAL_AP_SHOT.get(), CBCMSDualCannonProjectiles.NORMAL_AP_SHOT,
			CBCMSDualCannonPropertyTypes.NORMAL_AP_SHOT, DualCannonBehavior.Kind.AP_SHOT, 40, false, false,
			"normal_ap_shot_block");
		put(entries, CBCMSBlocks.EXTENDED_AP_SHOT.get(), CBCMSDualCannonProjectiles.EXTENDED_AP_SHOT,
			CBCMSDualCannonPropertyTypes.EXTENDED_AP_SHOT, DualCannonBehavior.Kind.AP_SHOT, 20, false, false,
			"normal_ap_shot_block");

		put(entries, CBCMSBlocks.NORMAL_HE_SHELL.get(), CBCMSDualCannonProjectiles.NORMAL_HE_SHELL,
			CBCMSDualCannonPropertyTypes.NORMAL_HE_SHELL, DualCannonBehavior.Kind.HE, 20, true, false,
			"normal_he_shell_block");
		put(entries, CBCMSBlocks.NORMAL_ANTIAIR_HE_SHELL.get(), CBCMSDualCannonProjectiles.NORMAL_ANTIAIR_HE_SHELL,
			CBCMSDualCannonPropertyTypes.NORMAL_ANTIAIR_HE_SHELL, DualCannonBehavior.Kind.ANTIAIR_HE, 0, true, false,
			"normal_antiair_he_shell_block");
		put(entries, CBCMSBlocks.EXTENDED_ANTIAIR_HE_SHELL.get(), CBCMSDualCannonProjectiles.EXTENDED_ANTIAIR_HE_SHELL,
			CBCMSDualCannonPropertyTypes.EXTENDED_ANTIAIR_HE_SHELL, DualCannonBehavior.Kind.ANTIAIR_HE, 0, true, false,
			"normal_antiair_he_shell_block");
		put(entries, CBCMSBlocks.NORMAL_AP_SHELL.get(), CBCMSDualCannonProjectiles.NORMAL_AP_SHELL,
			CBCMSDualCannonPropertyTypes.NORMAL_AP_SHELL, DualCannonBehavior.Kind.APHE, 30, true, false,
			"normal_ap_shell_block");
		put(entries, CBCMSBlocks.NORMAL_APBC_SHELL.get(), CBCMSDualCannonProjectiles.NORMAL_APBC_SHELL,
			CBCMSDualCannonPropertyTypes.NORMAL_APBC_SHELL, DualCannonBehavior.Kind.APBC, 30, true, false,
			"normal_apbc_shell_block");
		put(entries, CBCMSBlocks.NORMAL_SAP_SHELL.get(), CBCMSDualCannonProjectiles.NORMAL_SAP_SHELL,
			CBCMSDualCannonPropertyTypes.NORMAL_SAP_SHELL, DualCannonBehavior.Kind.SAP, 30, true, false,
			"normal_sap_shell_block");
		put(entries, CBCMSBlocks.NORMAL_INCENDIARY_HE_SHELL.get(),
			CBCMSDualCannonProjectiles.NORMAL_INCENDIARY_HE_SHELL,
			CBCMSDualCannonPropertyTypes.NORMAL_INCENDIARY_HE_SHELL, DualCannonBehavior.Kind.INCENDIARY, 20, true,
			false, "normal_incendiary_he_shell_block");

		return Map.copyOf(entries);
	}

	private static void put(Map<Block, Entry> entries, Block block, ProjectileType<DualCannonState> projectileType,
							MunitionPropertyType<? extends DualCannonMunitionProperties> propertyType,
							DualCannonBehavior.Kind kind, int baseLifetimeTicks, boolean fuzed, boolean baseFuze,
							String renderedBlockPath) {
		entries.put(block, new Entry(projectileType, propertyType, kind,
			new CBCMSDualCannonLaunchProfile(baseLifetimeTicks), fuzed, baseFuze,
			Cbcmoreshells.resource(renderedBlockPath)));
	}

}
