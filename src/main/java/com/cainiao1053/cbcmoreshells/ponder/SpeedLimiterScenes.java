package com.cainiao1053.cbcmoreshells.ponder;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class SpeedLimiterScenes {

	public static void speedLimiterIntro(SceneBuilder builder, SceneBuildingUtil util){
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		scene.title("speed_limiter/speed_limiter_intro", "Speed Limiter Intro");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		Selection gear = util.select().position(2,1,2);
		Selection handle = util.select().position(2,1,1);
		Selection basic = util.select().fromTo(2,1,1, 2, 1, 4);
		Selection press = util.select().position(2,1,4);

		scene.world().showSection(basic, Direction.DOWN);
		scene.idle(10);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.NORTH));
		scene.idle(80);

		scene.overlay().showControls(util.vector()
						.blockSurface(util.grid().at(2, 1, 1), Direction.NORTH), Pointing.DOWN, 10)
				.rightClick();
		scene.idle(10);

		scene.world().setKineticSpeed(handle, 32);
		scene.idle(3);
		scene.world().setKineticSpeed(handle, 0);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 1, 1), Direction.NORTH));
		scene.idle(80);

		scene.overlay().showControls(util.vector()
						.blockSurface(util.grid().at(2, 1, 1), Direction.NORTH), Pointing.DOWN, 10)
				.rightClick();
		scene.idle(10);

		scene.world().modifyBlock(
				new BlockPos(2,1,2),
				state -> state.setValue(BlockStateProperties.POWERED, true),
				false
		);
		scene.world().setKineticSpeed(handle, 32);
		scene.world().setKineticSpeed(gear, 32);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("bbb")
				.pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.NORTH));
		scene.idle(80);

		scene.world().hideSection(press, Direction.NORTH);
		scene.idle(15);

		scene.overlay().showControls(util.vector()
						.blockSurface(util.grid().at(2, 1, 2), Direction.NORTH), Pointing.DOWN, 10)
				.withItem(AllItems.WRENCH.asStack())
				.rightClick();
		scene.idle(20);

		scene.world().modifyBlock(
				new BlockPos(2,1,2),
				state -> state.setValue(BlockStateProperties.POWERED, false),
				false
		);

	}

}
