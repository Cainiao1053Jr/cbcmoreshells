package com.cainiao1053.cbcmoreshells;

import java.util.Optional;
import java.util.function.Supplier;

import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public enum CBCMSModsNeoForge {
	SABLE;

	private final String id;

	CBCMSModsNeoForge() {
		this.id = CreateLang.asId(name());
	}

	public String id() {
		return this.id;
	}

	public ResourceLocation resource(String path) {
		return CBCUtils.location(id, path);
	}

	public Block getBlock(String id) {
		return BuiltInRegistries.BLOCK.get(this.resource(id));
	}

	public boolean isLoaded() {
		return ModList.get().isLoaded(this.id);
	}

	public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
		return this.isLoaded() ? Optional.of(toRun.get().get()) : Optional.empty();
	}

	public void executeIfInstalled(Supplier<Runnable> toExecute) {
		if (isLoaded()) toExecute.get().run();
	}

}
