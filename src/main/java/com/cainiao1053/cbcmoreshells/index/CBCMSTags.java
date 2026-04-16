package com.cainiao1053.cbcmoreshells.index;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CBCMSTags {

	public static class CBCMSBlockTags {
		public static final TagKey<Block>
			COMBAT_COMMAND_EQUIPMENT = makeTag("combat_command_equipment"),
		    RACK_STABILIZER = makeTag("rack_stabilizer"),
			MAGAZINE_EQUIPMENT = makeTag("magazine_equipment")
			;

		public static TagKey<Block> makeTag(String path) {
			TagKey<Block> tag = TagKey.create(getBlockRegistryKey(), Cbcmoreshells.resource(path));
			//REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> ((TagsProvider<Block>) prov).tag(tag));
			return tag;
		}

		public static ResourceKey<Registry<Block>> getBlockRegistryKey() {
			return Registries.BLOCK;
		}

		public static void sectionRegister() {
		}

	}

	public static class CBCMSItemTags {
		//public static final TagKey<Item>
			;
	}

	public static void register() {
		CBCMSBlockTags.sectionRegister();
	}

//	private static <T> void addTag(TagAppender<T> app, TagKey<T> tag) {
//		TagAppenderAccessor accessor = (TagAppenderAccessor) app;
//		TagBuilder builder = accessor.getBuilder();
//		builder.add(new ForcedTagEntry(TagEntry.tag(tag.location())));
//	}

}
