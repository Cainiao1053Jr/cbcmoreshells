package com.cainiao1053.cbcmoreshells.mixin;

import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = FilteringBehaviour.class, remap = false)
public interface FilteringBehaviourAccessor {

	@Accessor("slotPositioning")
	void cbcms$setSlotPositioning(ValueBoxTransform slotPositioning);

}
