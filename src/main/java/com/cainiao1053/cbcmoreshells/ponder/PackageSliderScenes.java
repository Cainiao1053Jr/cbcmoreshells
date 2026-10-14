package com.cainiao1053.cbcmoreshells.ponder;

import com.simibubi.create.content.logistics.box.PackageStyles;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class PackageSliderScenes {

	public static void packageSliderIntro(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		scene.title("package_slider/package_slider_intro", "Package Slider");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		BlockPos mountPos = util.grid().at(2, 1, 2);
		BlockPos breechPos = util.grid().at(1, 2, 2);
		BlockPos muzzlePos = util.grid().at(4, 2, 2);
		BlockPos leverPos = util.grid().at(2, 1, 1);
		BlockPos armPos = util.grid().at(0, 1, 1);
		BlockPos chutePos = util.grid().at(4, 1, 4);

		Selection mount = util.select().position(mountPos);
		Selection cannon = util.select().fromTo(1, 2, 2, 4, 2, 2);
		Selection lever = util.select().position(leverPos);
		Selection arm = util.select().fromTo(0, 1, 0, 0, 1, 1);
		Selection chute = util.select().position(chutePos);

		// Building the cannon
		scene.world().showSection(mount, Direction.DOWN);
		scene.idle(10);
		scene.world().showSection(cannon, Direction.DOWN);
		scene.idle(15);
	}

}
