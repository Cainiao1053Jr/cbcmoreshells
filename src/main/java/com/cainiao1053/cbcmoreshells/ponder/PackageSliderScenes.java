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

	/**
	 * Expects {@code ponder/package_slider/package_slider_intro.nbt} on a 5x5 base plate:
	 * <ul>
	 * <li>cannon mount at (2, 1, 2), with the package slider on top along X: breech at (1, 2, 2), barrels at
	 * (2..4, 2, 2);</li>
	 * <li>redstone (e.g. a lever) driving the mount at (2, 1, 1);</li>
	 * <li>mechanical arm and depot feeding the mount at (0, 1, 1) and (0, 1, 0);</li>
	 * <li>a chute near the landing side at (4, 1, 4).</li>
	 * </ul>
	 */
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

		scene.overlay().showText(90)
			.attachKeyFrame()
			.text("Package sliders fire Create packages. They must be assembled on a cannon mount, and need one quick-firing breech and at least one barrel")
			.pointAt(util.vector().blockSurface(breechPos, Direction.NORTH));
		scene.idle(100);

		// Loading by hand
		scene.overlay().showControls(util.vector().blockSurface(breechPos, Direction.WEST), Pointing.DOWN, 30)
			.withItem(PackageStyles.getDefaultBox())
			.rightClick();
		scene.overlay().showText(90)
			.attachKeyFrame()
			.text("Right-click the breech with a package to load it. No propellant is needed; right-click with an empty hand to take the package back out")
			.pointAt(util.vector().blockSurface(breechPos, Direction.WEST));
		scene.idle(100);

		// Length
		scene.overlay().showText(90)
			.attachKeyFrame()
			.text("Longer cannons fire packages faster, but take longer to reload after each shot")
			.pointAt(util.vector().blockSurface(muzzlePos, Direction.NORTH));
		scene.idle(100);

		// Firing
		scene.world().showSection(lever, Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(70)
			.attachKeyFrame()
			.text("Power the cannon mount with redstone to fire. The package slider has no recoil")
			.pointAt(util.vector().blockSurface(leverPos, Direction.UP));
		scene.idle(80);

		// Automatic loading
		scene.world().showSection(arm, Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(90)
			.attachKeyFrame()
			.text("Mechanical arms can load packages through the cannon mount once the breech has finished reloading")
			.pointAt(util.vector().blockSurface(armPos, Direction.UP));
		scene.idle(100);

		// Landing
		scene.world().showSection(chute, Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(100)
			.attachKeyFrame()
			.text("On landing, the package goes into the nearest chute around the impact point, or drops to the ground if there is none. Packages that hit too fast break apart and spill their contents")
			.pointAt(util.vector().blockSurface(chutePos, Direction.UP));
		scene.idle(110);
	}

}
