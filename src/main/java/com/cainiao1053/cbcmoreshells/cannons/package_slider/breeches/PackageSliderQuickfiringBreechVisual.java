package com.cainiao1053.cbcmoreshells.cannons.package_slider.breeches;

import com.cainiao1053.cbcmoreshells.index.CBCMSBlockPartials;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Vector3f;

import java.util.function.Consumer;

/**
 * Draws the slider block of the package slider breech. The partial is modelled for a breech facing up, sitting
 * centred on the bottom face (10 x 10 wide, 8 tall); it is rotated to the breech facing, so at rest it sits centred
 * on the breech's back face. When the cannon fires it is pushed to the centre of the frontmost barrel and travels
 * back to rest as the reload progresses.
 */
public class PackageSliderQuickfiringBreechVisual extends AbstractBlockEntityVisual<PackageSliderQuickfiringBreechBlockEntity>
	implements SimpleDynamicVisual {

	private final OrientedInstance slider;
	private final Direction facing;

	public PackageSliderQuickfiringBreechVisual(VisualizationContext ctx, PackageSliderQuickfiringBreechBlockEntity blockEntity,
												float partialTick) {
		super(ctx, blockEntity, partialTick);
		this.facing = this.blockState.getValue(BlockStateProperties.FACING);
		this.slider = this.instancerProvider()
			.instancer(InstanceTypes.ORIENTED, Models.partial(CBCMSBlockPartials.PACKAGE_SLIDER_SLIDER_BLOCK))
			.createInstance();
		// Direction#getRotation maps up onto the given direction, about the block centre.
		this.slider.rotation(this.facing.getRotation());
		this.transformModels(partialTick);
	}

	@Override
	public void beginFrame(DynamicVisual.Context ctx) {
		this.transformModels(ctx.partialTick());
	}

	private void transformModels(float partialTick) {
		Vector3f offset = this.facing.step().mul(this.blockEntity.getSliderOffset(partialTick));
		this.slider.position(this.getVisualPosition())
			.translatePosition(offset.x(), offset.y(), offset.z())
			.setChanged();
	}

	@Override
	public void updateLight(float partialTick) {
		this.relight(this.pos, this.slider);
	}

	@Override
	protected void _delete() {
		this.slider.delete();
	}

	@Override
	public void collectCrumblingInstances(Consumer<Instance> consumer) {
		consumer.accept(this.slider);
	}

}
