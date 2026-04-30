package com.cainiao1053.cbcmoreshells.ponder;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.CBCMSItems;
import com.cainiao1053.cbcmoreshells.blocks.ammo_rack.AmmoRackBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemStackHandler;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class AmmoRackScenes {

	public static void ammoRackIntro(SceneBuilder builder, SceneBuildingUtil util){
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		scene.title("ammo_rack/ammo_rack_intro", "Ammo Rack Intro");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		Selection rack = util.select().position(2,1,1);
		Selection button = util.select().position(2,1,0);
		Selection basic = util.select().fromTo(0,1,1, 4, 3, 2);

		scene.world().showIndependentSection(basic, Direction.DOWN);
		scene.idle(10);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 1, 1), Direction.NORTH));
		scene.idle(80);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(4, 1, 1), Direction.NORTH));
		scene.idle(80);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("bbb")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 1, 1), Direction.NORTH));
		scene.idle(80);

		scene.overlay().showControls(util.vector()
						.blockSurface(util.grid().at(2, 1, 1), Direction.NORTH), Pointing.DOWN, 20)
				.withItem(CBCMSBlocks.NORMAL_AP_SHELL.asStack())
				.rightClick();

		ItemStackHandler inventory1 = new ItemStackHandler(6);
		inventory1.setStackInSlot(0, CBCMSBlocks.NORMAL_AP_SHELL.asStack());
		scene.world().modifyBlockEntityNBT(rack, AmmoRackBlockEntity.class, tag -> {
			tag.put("Inventory", inventory1.serializeNBT(scene.world().getHolderLookupProvider()));
		});
		scene.idle(30);

		scene.overlay().showControls(util.vector()
						.blockSurface(util.grid().at(2, 1, 1), Direction.NORTH), Pointing.DOWN, 20)
				.withItem(CBCMSBlocks.NORMAL_SAP_SHELL.asStack())
				.rightClick();
		ItemStackHandler inventory2 = new ItemStackHandler(6);
		inventory2.setStackInSlot(0, CBCMSBlocks.NORMAL_AP_SHELL.asStack());
		inventory2.setStackInSlot(1, CBCMSBlocks.NORMAL_SAP_SHELL.asStack());
		scene.world().modifyBlockEntityNBT(rack, AmmoRackBlockEntity.class, tag -> {
			tag.put("Inventory", inventory2.serializeNBT(scene.world().getHolderLookupProvider()));
		});
		scene.idle(30);

		scene.overlay().showText(50)
				.attachKeyFrame()
				.text("bbb")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 1, 1), Direction.NORTH));
		scene.idle(60);

		scene.world().modifyBlockEntityNBT(rack, AmmoRackBlockEntity.class, tag -> {
			tag.put("Inventory", inventory1.serializeNBT(scene.world().getHolderLookupProvider()));
		});
		scene.overlay().showControls(util.vector()
						.blockSurface(util.grid().at(2, 1, 1), Direction.NORTH), Pointing.DOWN, 20)
				.rightClick();
		scene.idle(50);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("bbb")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 2, 1), Direction.NORTH));
		scene.idle(80);
		scene.overlay().showControls(util.vector()
						.blockSurface(util.grid().at(2, 1, 1), Direction.NORTH), Pointing.DOWN, 10)
				.withItem(CBCMSBlocks.NORMAL_SAP_SHELL.asStack())
				.rightClick();
		scene.world().modifyBlockEntityNBT(rack, AmmoRackBlockEntity.class, tag -> {
			tag.put("Inventory", inventory2.serializeNBT(scene.world().getHolderLookupProvider()));
		});
		scene.idle(20);
		scene.overlay().showControls(util.vector()
						.blockSurface(util.grid().at(2, 2, 1), Direction.NORTH), Pointing.DOWN, 20)
				.rightClick();

		scene.idle(30);

	}

}
