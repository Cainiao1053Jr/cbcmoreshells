//package com.cainiao1053.cbcmoreshells.recipes;
//
//import com.cainiao1053.cbcmoreshells.index.CBCMSRecipeTypes;
//import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shelless_he_shell.ShellessHEProjectileBlockItem;
//import net.minecraft.core.RegistryAccess;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.Container;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeSerializer;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.Level;
//import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;
//
//public class ShellFuzingDeployerRecipe implements Recipe<Container> {
//
//	private final ItemStack munition;
//	private final ItemStack fuze;
//
//	public ShellFuzingDeployerRecipe() {
//		this.munition = ItemStack.EMPTY;
//		this.fuze = ItemStack.EMPTY;
//	}
//
//	public ShellFuzingDeployerRecipe(ItemStack munition, ItemStack fuze) {
//		this.munition = munition.copy();
//		this.fuze = fuze.copy();
//	}
//
//	@Override
//	public boolean matches(Container container, Level level) {
//		if (!(this.fuze.getItem() instanceof FuzeItem)) return false;
//		if (this.munition.getItem() instanceof ShellessHEProjectileBlockItem) {
//			return !this.munition.getOrCreateTag().contains("Fuze", Tag.TAG_COMPOUND);
//		}
////		if (this.munition.getItem() instanceof AutocannonCartridgeItem) {
////			ItemStack cartridgeRound = AutocannonCartridgeItem.getProjectileStack(this.munition);
////			return !cartridgeRound.isEmpty() && cartridgeRound.getItem() instanceof FuzedItemMunition
////				&& !cartridgeRound.getOrCreateTag().contains("Fuze", Tag.TAG_COMPOUND);
////		}
//		return false;
//	}
//
//	@Override public ItemStack assemble(Container inv, RegistryAccess access) { return this.getResultItem(access); }
//
//	@Override
//	public ItemStack getResultItem(RegistryAccess access) {
//		ItemStack result = this.munition.copy();
//		result.setCount(1);
//		ItemStack fuzeCopy = this.fuze.copy();
//		fuzeCopy.setCount(1);
//		CompoundTag tag = result.getOrCreateTag();
//		if (result.getItem() instanceof ShellessHEProjectileBlockItem) {
//			tag.put("Fuze", fuzeCopy.save(new CompoundTag()));
//		}
//		return result;
//	}
//
//	@Override public boolean canCraftInDimensions(int width, int height) { return true; }
//
//	@Override public ResourceLocation getId() { return CBCMSRecipeTypes.SHELL_FUZING_DEPLOYER.getId(); }
//	@Override public RecipeSerializer<?> getSerializer() { return CBCMSRecipeTypes.SHELL_FUZING_DEPLOYER.getSerializer(); }
//	@Override public RecipeType<?> getType() { return CBCMSRecipeTypes.SHELL_FUZING_DEPLOYER.getType(); }
//
//}
