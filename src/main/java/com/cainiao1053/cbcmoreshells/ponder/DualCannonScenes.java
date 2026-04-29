package com.cainiao1053.cbcmoreshells.ponder;

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

public class DualCannonScenes {

	public static void dualCannonIntro(SceneBuilder builder, SceneBuildingUtil util){
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		scene.title("dual_cannon/dual_cannon_intro", "Dual Cannon Intro");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		List<ElementLink<WorldSectionElement>> setupBlocks = new ArrayList<>();
		ElementLink<WorldSectionElement> cannonSetup = scene.world().showIndependentSection(util.select().fromTo(0, 0, 2, 5, 3, 2), Direction.DOWN);
		//scene.world().moveSection(cannonSetup, down, 0);
		setupBlocks.add(cannonSetup);
		scene.idle(10);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 3, 2), Direction.NORTH));
		scene.idle(80);

		scene.rotateCameraY(-90.0f);
		scene.idle(15);
		ElementLink<WorldSectionElement> munitions = scene.world().showIndependentSection(util.select().fromTo(2, 1, 4, 4, 1, 4), Direction.DOWN);
		setupBlocks.add(munitions);
		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("bbb")
				.pointAt(util.vector().blockSurface(util.grid().at(3, 1, 4), Direction.NORTH));
		scene.idle(80);
		scene.rotateCameraY(180.0f);
		scene.idle(15);

		ElementLink<WorldSectionElement> mechanicalArm = scene.world().showIndependentSection(util.select().fromTo(3, 1, 0, 4, 1, 1), Direction.DOWN);
		setupBlocks.add(mechanicalArm);
		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("ccc")
				.pointAt(util.vector().blockSurface(util.grid().at(4, 1, 0), Direction.NORTH));
		scene.idle(80);
		scene.world().setKineticSpeed(util.select().position(4, 1, 0), 128.0f);
		scene.overlay().showText(40)
				.attachKeyFrame()
				.text("ddd")
				.pointAt(util.vector().blockSurface(util.grid().at(4, 1, 1), Direction.NORTH));
		scene.idle(50);
	}

	public static void dualCannonSetting(SceneBuilder builder, SceneBuildingUtil util){
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		scene.title("dual_cannon/dual_cannon_setting", "Dual Cannon Intro");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		Selection basic = util.select().fromTo(0,2,0, 4, 2, 0);
		Selection duals = util.select().fromTo(0,2,2, 4, 2, 4);
		Selection steel_single = util.select().fromTo(0,1,0, 4, 1, 0);
		Selection singles = util.select().fromTo(0,1,2, 4, 1, 4);

		List<ElementLink<WorldSectionElement>> cannonBlocks = new ArrayList<>();
		ElementLink<WorldSectionElement> cannon_basic = scene.world().showIndependentSection(basic, Direction.DOWN);
		cannonBlocks.add(cannon_basic);
		scene.idle(10);
		ElementLink<WorldSectionElement> cannon_duals = scene.world().showIndependentSection(duals, Direction.DOWN);
		cannonBlocks.add(cannon_duals);
		scene.idle(10);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 2, 0), Direction.NORTH));
		scene.idle(80);

		scene.overlay().showText(60)
				.attachKeyFrame()
				.text("bbb")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.NORTH));
		scene.idle(70);

		scene.overlay().showText(60)
				.attachKeyFrame()
				.text("ccc")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 2, 4), Direction.NORTH));
		scene.idle(70);

		scene.world().hideIndependentSection(cannon_duals, Direction.DOWN);
		scene.idle(10);

		ElementLink<WorldSectionElement> cannon_steel_single = scene.world().showIndependentSection(steel_single, Direction.DOWN);
		cannonBlocks.add(cannon_steel_single);
		scene.idle(10);
		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("ddd")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 1, 0), Direction.NORTH));
		scene.idle(80);

		scene.world().hideIndependentSection(cannon_basic, Direction.DOWN);
		ElementLink<WorldSectionElement> cannon_singles = scene.world().showIndependentSection(singles, Direction.DOWN);
		cannonBlocks.add(cannon_singles);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("ddd")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.NORTH));
		scene.idle(80);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("ddd")
				.pointAt(util.vector().blockSurface(util.grid().at(4, 1, 3), Direction.NORTH));
		scene.idle(80);
	}

	public static void dualCannonCommand(SceneBuilder builder, SceneBuildingUtil util){
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		scene.title("dual_cannon/dual_cannon_command", "Dual Cannon Command");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		Selection basic = util.select().fromTo(0,1,0, 4, 3, 4);
		ElementLink<WorldSectionElement> cannon_basic = scene.world().showIndependentSection(basic, Direction.DOWN);
		scene.idle(10);

		scene.overlay().showText(90)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 3, 2), Direction.NORTH));
		scene.idle(100);

//		scene.rotateCameraY(-90);
//		scene.overlay().showText(70)
//				.attachKeyFrame()
//				.text("aaa")
//				.pointAt(util.vector().blockSurface(util.grid().at(4, 1, 4), Direction.NORTH));
//		scene.idle(80);
//		scene.rotateCameraY(90);

		scene.overlay().showText(80)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.NORTH));
		scene.idle(20);
		scene.overlay().showControls(util.vector()
				.blockSurface(util.grid().at(2, 1, 2), Direction.NORTH), Pointing.DOWN, 40)
				.withItem(CBCMSItems.RELOAD_COMBAT_COMMAND.asStack()).rightClick();
		scene.idle(70);

		scene.overlay().showText(90)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(3, 1, 0), Direction.NORTH));
		scene.idle(20);
		scene.overlay().showControls(util.vector()
						.blockSurface(util.grid().at(3, 1, 0), Direction.NORTH), Pointing.DOWN, 20)
				.withItem(CBCMSItems.RELOAD_COMBAT_COMMAND.asStack()).rightClick();
		scene.idle(20);
		scene.overlay().showControls(util.vector()
				.blockSurface(util.grid().at(2, 1, 0), Direction.NORTH), Pointing.DOWN, 20)
				.rightClick();
		scene.idle(40);
	}

	public static void dualCannonMagazine(SceneBuilder builder, SceneBuildingUtil util){
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		scene.title("dual_cannon/dual_cannon_magazine", "Dual Cannon Magazine");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		Selection basic = util.select().fromTo(0,1,2, 4, 3, 2);
		Selection arm = util.select().fromTo(2,1,0, 2, 1, 0);
		Selection ammo = util.select().fromTo(3,1,0, 3, 3, 0);
		ElementLink<WorldSectionElement> cannon_basic = scene.world().showIndependentSection(basic, Direction.DOWN);

		scene.idle(10);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(4, 3, 2), Direction.NORTH));
		scene.idle(80);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(4, 3, 2), Direction.NORTH));
		scene.idle(80);

		scene.world().showIndependentSection(ammo, Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(3, 2, 0), Direction.NORTH));
		scene.idle(80);

		scene.world().showIndependentSection(arm, Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 1, 0), Direction.NORTH));
		scene.idle(80);
	}

	private static <T extends Comparable<T>> UnaryOperator<BlockState> setStateValue(Property<T> property, T value) {
		return state -> state.hasProperty(property) ? state.setValue(property, value) : state;
	}

}
