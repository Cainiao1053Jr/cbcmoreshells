package com.cainiao1053.cbcmoreshells.ponder;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.CBCMSItems;
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

public class ProjectileRackScenes {

	public static void projectileRackIntro(SceneBuilder builder, SceneBuildingUtil util){
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		scene.title("projectile_rack/projectile_rack_intro", "Projectile Rack Intro");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		Selection rack = util.select().fromTo(0,1,2, 4, 2, 2);
		Selection bomb = util.select().fromTo(1,1,4, 4, 1, 4);
		Selection arm = util.select().fromTo(4,1,0, 4, 1, 1);

		scene.world().showIndependentSection(rack, Direction.DOWN);
		scene.idle(10);

		scene.overlay().showText(90)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.NORTH));
		scene.idle(100);

		scene.overlay().showText(50)
				.attachKeyFrame()
				.text("bbb")
				.pointAt(util.vector().blockSurface(util.grid().at(3, 2, 2), Direction.NORTH));
		scene.overlay().showControls(util.vector()
						.blockSurface(util.grid().at(3, 2, 2), Direction.NORTH), Pointing.DOWN, 20)
				.rightClick();
		scene.idle(30);
		scene.overlay().showControls(util.vector()
						.blockSurface(util.grid().at(3, 2, 2), Direction.NORTH), Pointing.DOWN, 20)
				.withItem(CBCMSBlocks.HE_BOMB.asStack())
				.rightClick();
		scene.idle(30);

		scene.world().showIndependentSection(bomb, Direction.DOWN);
		scene.rotateCameraY(-90);
		scene.idle(15);

		scene.overlay().showText(80)
				.attachKeyFrame()
				.text("ccc")
				.pointAt(util.vector().blockSurface(util.grid().at(4, 1, 4), Direction.NORTH));
		scene.idle(90);

		scene.overlay().showText(80)
				.attachKeyFrame()
				.text("ccc")
				.pointAt(util.vector().blockSurface(util.grid().at(1, 1, 4), Direction.NORTH));
		scene.idle(90);

		scene.rotateCameraY(180);
		scene.idle(20);

		scene.world().showIndependentSection(arm, Direction.DOWN);
		scene.idle(10);

		scene.overlay().showText(90)
				.attachKeyFrame()
				.text("ccc")
				.pointAt(util.vector().blockSurface(util.grid().at(4, 1, 0), Direction.NORTH));
		scene.idle(100);

	}

}
