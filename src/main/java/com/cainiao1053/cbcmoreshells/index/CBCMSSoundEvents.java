package com.cainiao1053.cbcmoreshells.index;

import static com.simibubi.create.AllSoundEvents.SoundEntry;
import static com.simibubi.create.AllSoundEvents.SoundEntryBuilder;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class CBCMSSoundEvents {

	public static final Map<ResourceLocation, SoundEntry> ALL = new HashMap<>();

	public static final SoundEntry
		ROCKET_LAUNCH = create("rocket_launch").subtitle("Rocket Launch")
			.category(SoundSource.BLOCKS)
			.build();

//	public static final SoundEntry
//			SONAR_PULSE = create("sonar_pulse").subtitle("Sonar Pulse")
//			.category(SoundSource.BLOCKS)
//			.build();

	public static final SoundEntry
			SERVICEBELL = create("servicebell").subtitle("Servicebell")
			.category(SoundSource.BLOCKS)
			.build();

	public static final SoundEntry
			DUAL_CANNON_1 = create("dual_cannon_1").subtitle("Dual Cannon 1")
			.category(SoundSource.BLOCKS)
			.build();

	public static final SoundEntry
			DUAL_CANNON_2 = create("dual_cannon_2").subtitle("Dual Cannon 2")
			.category(SoundSource.BLOCKS)
			.build();

	public static final SoundEntry
			DUAL_CANNON_3 = create("dual_cannon_3").subtitle("Dual Cannon 3")
			.category(SoundSource.BLOCKS)
			.build();


	private static SoundEntryBuilder create(String id) {
		return new CBCMSSoundEntryBuilder(Cbcmoreshells.resource(id));
	}

	public static void prepare() {
		for (SoundEntry entry : ALL.values())
			entry.prepare();
	}

	public static void register(Consumer<SoundEntry> consumer) {
		for (SoundEntry entry : ALL.values())
			consumer.accept(entry);
	}

	public static void registerLangEntries() {
		for (SoundEntry entry : ALL.values()) {
			if (entry.hasSubtitle())
				Cbcmoreshells.REGISTRATE.addRawLang(entry.getSubtitleKey(), entry.getSubtitle());
		}
	}

	public static class CBCMSSoundEntryBuilder extends SoundEntryBuilder {
		public CBCMSSoundEntryBuilder(ResourceLocation id) {
			super(id);
		}

		@Override
		public SoundEntryBuilder addVariant(String name) {
			return this.addVariant(Cbcmoreshells.resource(name));
		}

		@Override
		public SoundEntry build() {
			SoundEntry entry = super.build();
			ALL.put(entry.getId(), entry);
			return entry;
		}
	}

	public static SoundEntryProvider provider(PackOutput output) {
		return new SoundEntryProvider(output);
	}

	public static class SoundEntryProvider implements DataProvider {
		private PackOutput output;

		public SoundEntryProvider(PackOutput output) {
			this.output = output;
		}

		@Override
		public CompletableFuture<?> run(CachedOutput cache) {
			Path path = this.output.getOutputFolder().resolve("assets/" + Cbcmoreshells.MODID);
			JsonObject json = new JsonObject();
			ALL.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> entry.getValue().write(json));
			return DataProvider.saveStable(cache, json, path.resolve("sounds.json"));
		}

		@Override public String getName() { return "CBCMS Sounds"; }
	}

}
