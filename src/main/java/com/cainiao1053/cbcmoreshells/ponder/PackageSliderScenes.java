package com.cainiao1053.cbcmoreshells.ponder;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.PackageSliderBlockEntity;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.content.logistics.box.PackageStyles;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class PackageSliderScenes {

	public static void packageSliderIntro(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		scene.title("package_slider/package_slider_intro", "Package Slider");
		scene.configureBasePlate(0, 0, 13);
		scene.showBasePlate();

		BlockPos mountPos = util.grid().at(10, 1, 6);
		BlockPos breechPos = util.grid().at(11, 3, 6);
		BlockPos rackPos = util.grid().at(10, 3, 6);
		BlockPos packagePos = util.grid().at(2, 3, 6);
		BlockPos armPos = util.grid().at(9, 1, 3);
		BlockPos chutePos = util.grid().at(3, 2, 6);

		Selection mount = util.select().position(mountPos);
		Selection cannon = util.select().fromTo(8, 1, 6, 11, 3, 6);
		Selection target = util.select().fromTo(0, 1, 6, 2, 3, 8);
		Selection loader = util.select().fromTo(9, 1, 1, 10, 3, 3);
		Selection chute = util.select().fromTo(3, 1, 6, 3, 3, 6);

		Item iCardboard = BuiltInRegistries.ITEM.get(
				ResourceLocation.fromNamespaceAndPath("create", "rare_creeper_package")
		);
		ItemStack cardboard = new ItemStack(iCardboard);

		scene.world().showSection(cannon, Direction.DOWN);
		scene.world().showSection(loader, Direction.DOWN);
		scene.idle(10);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(mountPos, Direction.NORTH));
		scene.idle(80);

		scene.rotateCameraY(90);
		scene.idle(25);

		scene.overlay().showText(50)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(breechPos, Direction.NORTH));
		scene.idle(60);

		scene.overlay().showControls(util.vector()
						.blockSurface(breechPos, Direction.NORTH), Pointing.DOWN, 20)
				.withItem(cardboard)
				.rightClick();

		scene.world().modifyBlockEntity(rackPos, PackageSliderBlockEntity.class, be ->{
			be.cannonBehavior().setLoaded(cardboard);
		});
		scene.idle(30);

		scene.rotateCameraY(-90);
		scene.idle(25);

		scene.overlay().showText(80)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(armPos, Direction.NORTH));
		scene.idle(90);

		scene.world().showSection(target, Direction.DOWN);
		scene.idle(15);

		scene.overlay().showText(80)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(mountPos, Direction.NORTH));
		scene.idle(90);

		scene.overlay().showControls(util.vector()
						.blockSurface(mountPos, Direction.NORTH), Pointing.DOWN, 10)
				.withItem(new ItemStack(Items.REDSTONE))
				.rightClick();
		scene.world().modifyBlockEntity(rackPos, PackageSliderBlockEntity.class, be ->{
			be.cannonBehavior().removeLoaded();
		});

		ElementLink<EntityElement> pkg =  scene.world().createEntity(l ->{
			PackageEntity p = PackageEntity.fromItemStack(l, new Vec3(2.5,3,6.5), cardboard);
			return p;
		});

		scene.idle(90);

		scene.world().showSection(chute, Direction.DOWN);
		scene.world().modifyEntity(pkg, Entity::discard);
		scene.world().modifyBlockEntity(rackPos, PackageSliderBlockEntity.class, be ->{
			be.cannonBehavior().setLoaded(cardboard);
		});
		scene.idle(15);

		scene.overlay().showText(80)
				.attachKeyFrame()
				.text("aaa")
				.pointAt(util.vector().blockSurface(chutePos, Direction.NORTH));
		scene.idle(90);

		scene.overlay().showControls(util.vector()
						.blockSurface(mountPos, Direction.NORTH), Pointing.DOWN, 10)
				.withItem(new ItemStack(Items.REDSTONE))
				.rightClick();
		scene.world().modifyBlockEntity(rackPos, PackageSliderBlockEntity.class, be ->{
			be.cannonBehavior().removeLoaded();
		});
		scene.idle(10);

		scene.overlay().showControls(util.vector()
						.blockSurface(chutePos, Direction.NORTH), Pointing.DOWN, 10)
				.withItem(cardboard);
		scene.idle(70);
	}

}
