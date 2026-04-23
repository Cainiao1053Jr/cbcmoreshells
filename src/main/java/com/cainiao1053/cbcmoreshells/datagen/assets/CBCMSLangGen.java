//package com.cainiao1053.cbcmoreshells.datagen.assets;
//
//
//import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
//import com.tterrag.registrate.util.entry.BlockEntry;
//import com.tterrag.registrate.util.entry.ItemEntry;
//import com.tterrag.registrate.util.entry.ItemProviderEntry;
//import net.minecraft.resources.ResourceLocation;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//import static com.cainiao1053.cbcmoreshells.Cbcmoreshells.REGISTRATE;
//
//public class CBCMSLangGen {
//
//	public static void prepare() {
//
//		tooltip(CBCMSBlocks.Inferior_HE_SHELL)
//		.header("HIGH EXPLOSIVE (HE) SHELL")
//		.summary("Delivers _explosive_ force to the battlefield.")
//		.conditionAndBehavior("On Detonation", "Explodes with a bit less power than _TNT_.");
//
//	}
//
//
//
//
//
//	private static String capitalizeAll(String str) {
//		return Arrays.stream(str.split(" ")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
//	}
//
//	private static class TooltipBuilder {
//		private final ResourceLocation loc;
//		private final String type;
//		private int cbCount = 1;
//		private int caCount = 1;
//		public TooltipBuilder(ItemProviderEntry<?> provider, boolean item) {
//			this.loc = provider.getId();
//			this.type = item ? "item" : "block";
//		}
//
//		public TooltipBuilder header(String enUS) {
//			REGISTRATE.addLang(this.type, this.loc, "tooltip", enUS);
//			return this;
//		}
//
//		public TooltipBuilder summary(String enUS) {
//			REGISTRATE.addLang(this.type, this.loc, "tooltip.summary", enUS);
//			return this;
//		}
//
//		public TooltipBuilder conditionAndBehavior(String enUSCondition, String enUSBehaviour) {
//			REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.condition%d", this.cbCount), enUSCondition);
//			REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.behaviour%d", this.cbCount), enUSBehaviour);
//			this.cbCount++;
//			return this;
//		}
//
//		public TooltipBuilder controlAndAction(String enUSControl, String enUSAction) {
//			REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.control%d", this.caCount), enUSControl);
//			REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.action%d", this.caCount), enUSAction);
//			this.caCount++;
//			return this;
//		}
//	}
//
//	private static TooltipBuilder tooltip(BlockEntry<?> provider) {
//		return new TooltipBuilder(provider, false);
//	}
//
//	private static TooltipBuilder tooltip(ItemEntry<?> provider) {
//		return new TooltipBuilder(provider, true);
//	}
//
//}
