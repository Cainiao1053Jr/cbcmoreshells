//package com.cainiao1053.cbcmoreshells.index;
//
//import java.util.function.Function;
//import java.util.function.Supplier;
//
//import javax.annotation.Nullable;
//
//import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
//import com.cainiao1053.cbcmoreshells.recipes.ShellFuzingDeployerRecipe;
//import com.cainiao1053.cbcmoreshells.recipes.ShellFuzingRecipe;
//import com.google.gson.JsonObject;
//import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
//import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
//import com.tterrag.registrate.util.nullness.NonNullSupplier;
//
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeSerializer;
//import net.minecraft.world.item.crafting.RecipeType;
//
//import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
//
//public enum CBCMSRecipeTypes implements IRecipeTypeInfo {
//
//	SHELL_FUZING_DEPLOYER(noSerializer(r -> new ShellFuzingDeployerRecipe())),
//	SHELL_FUZING(noSerializer(ShellFuzingRecipe::new));
//
//	private final ResourceLocation id;
//	private final Supplier<RecipeSerializer<?>> serializerObject;
//	@Nullable
//	private final RecipeType<?> typeObject;
//	private final NonNullSupplier<RecipeType<?>> type;
//
//
//	CBCMSRecipeTypes(NonNullSupplier<RecipeSerializer<?>> serializerSupplier, NonNullSupplier<RecipeType<?>> typeSupplier, boolean registerType) {
//		String name = Lang.asId(name());
//		id = Cbcmoreshells.resource(name);
//		serializerObject = IndexPlatform.registerRecipeSerializer(this.id, serializerSupplier);
//		//serializerObject = serializerSupplier;
//
//		if (registerType) {
//			typeObject = typeSupplier.get();
//			IndexPlatform.registerRecipeType(this.id, typeSupplier);
//			type = typeSupplier;
//		} else {
//			typeObject = null;
//			type = typeSupplier;
//		}
//	}
//
//	CBCMSRecipeTypes(NonNullSupplier<RecipeSerializer<?>> serializerSupplier) {
//		String name = Lang.asId(name());
//		id = Cbcmoreshells.resource(name);
//		serializerObject = IndexPlatform.registerRecipeSerializer(this.id, serializerSupplier);
//		//serializerObject = serializerSupplier;
//		typeObject = simpleType(id);
//		type = () -> typeObject;
//		IndexPlatform.registerRecipeType(this.id, this.type);
//	}
//
//	CBCMSRecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory) {
//		this(() -> new ProcessingRecipeSerializer<>(processingFactory));
//	}
//
//	public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
//		String stringId = id.toString();
//		return new RecipeType<>() {
//			@Override
//			public String toString() {
//				return stringId;
//			}
//		};
//	}
//
//	@Override public ResourceLocation getId() { return this.id; }
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public <T extends RecipeSerializer<?>> T getSerializer() { return (T) this.serializerObject.get(); }
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public <T extends RecipeType<?>> T getType() { return (T) this.type.get(); }
//
//	public static void register() {
//	}
//
//	private static <T extends Recipe<?>> NonNullSupplier<RecipeSerializer<?>> noSerializer(Function<ResourceLocation, T> prov) {
//		return () -> new SimpleRecipeSerializer<>(prov);
//	}
//
//	private static class SimpleRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
//		private final Function<ResourceLocation, T> constructor;
//
//		public SimpleRecipeSerializer(Function<ResourceLocation, T> constructor) {
//			this.constructor = constructor;
//		}
//
//		@Override
//		public T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
//			return this.constructor.apply(recipeId);
//		}
//
//		@Override
//		public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
//			return this.constructor.apply(recipeId);
//		}
//
//		@Override
//		public void toNetwork(FriendlyByteBuf buffer, T recipe) {
//		}
//	}
//
//}
