package com.cainiao1053.cbcmoreshells.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.crafting.munition_assembly.MunitionFuzingRecipe;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

@Mixin(MunitionFuzingRecipe.class)
public abstract class MunitionFuzingRecipeMixin extends CustomRecipe {

	private MunitionFuzingRecipeMixin() {
		super(null);
	}

	@Inject(method = "matches", at = @At("HEAD"), cancellable = true, remap = false)
	private void injectedMatches(CraftingInput container, Level level, CallbackInfoReturnable<Boolean> cir) {
		ItemStack round = ItemStack.EMPTY;
		ItemStack fuze = ItemStack.EMPTY;

		for (int i = 0; i < container.size(); ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;

			if (stack.getItem() instanceof AutocannonCartridgeItem) {
				if (!round.isEmpty()) {
					cir.setReturnValue(false);
					return;
				}
				stack = AutocannonCartridgeItem.getProjectileStack(stack);
			}

			if (stack.getItem() instanceof FuzedItemMunition) {
				CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
				if (!round.isEmpty() || (customData != null && customData.copyTag().contains("Fuze"))) {
					cir.setReturnValue(false);
					return;
				}
				round = stack;
			} else if (stack.getItem() instanceof FuzedProjectileBlockItem) {
				CustomData blockData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
				if (!round.isEmpty() || (blockData != null && blockData.copyTag().contains("Fuze"))) {
					cir.setReturnValue(false);
					return;
				}
				round = stack;
			} else if (stack.getItem() instanceof FuzeItem) {
				if (!fuze.isEmpty()) {
					cir.setReturnValue(false);
					return;
				}
				fuze = stack;
			} else {
				cir.setReturnValue(false);
				return;
			}
		}

		cir.setReturnValue(!round.isEmpty() && !fuze.isEmpty());
	}

	@Inject(method = "assemble", at = @At("HEAD"), cancellable = true, remap = false)
	private void injectedAssemble(CraftingInput container, HolderLookup.Provider registries, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack round = ItemStack.EMPTY;
		ItemStack fuze = ItemStack.EMPTY;

		for (int i = 0; i < container.size(); ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;

			if (stack.getItem() instanceof FuzedItemMunition || stack.getItem() instanceof AutocannonCartridgeItem || stack.getItem() instanceof FuzedProjectileBlockItem) {
				if (!round.isEmpty()) {
					cir.setReturnValue(ItemStack.EMPTY);
					return;
				}
				round = stack;
			} else if (stack.getItem() instanceof FuzeItem) {
				if (!fuze.isEmpty()) {
					cir.setReturnValue(ItemStack.EMPTY);
					return;
				}
				fuze = stack;
			} else {
				cir.setReturnValue(ItemStack.EMPTY);
				return;
			}
		}

		if (round.isEmpty() || fuze.isEmpty()) {
			cir.setReturnValue(ItemStack.EMPTY);
			return;
		}

		ItemStack result = round.copy();
		result.setCount(1);
		ItemStack fuzeCopy = fuze.copy();
		fuzeCopy.setCount(1);

		if (result.getItem() instanceof FuzedItemMunition) {
			result.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> {
				CompoundTag nbt = data.copyTag();
				nbt.put("Fuze", fuzeCopy.save(registries));
				return CustomData.of(nbt);
			});
		} else if (result.getItem() instanceof AutocannonCartridgeItem) {
			result.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> {
				CompoundTag nbt = data.copyTag();
				CompoundTag projectile = nbt.getCompound("Projectile");
				CompoundTag projectileTag = projectile.getCompound("tag");
				projectileTag.put("Fuze", fuzeCopy.save(registries));
				projectile.put("tag", projectileTag);
				nbt.put("Projectile", projectile);
				return CustomData.of(nbt);
			});
		} else if (result.getItem() instanceof FuzedProjectileBlockItem) {
			result.update(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY, data -> {
				CompoundTag nbt = data.copyTag();
				nbt.put("Fuze", fuzeCopy.save(registries));
				nbt.putString("id", "createbigcannons:fuzed_block");
				return CustomData.of(nbt);
			});
		}

		cir.setReturnValue(result);
	}
}
