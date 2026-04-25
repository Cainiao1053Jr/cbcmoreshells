package com.cainiao1053.cbcmoreshells.mixin;

import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedDualCannonContraption;
import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedProjectileRackContraption;
import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedTorpedoTubeContraption;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.quick_firing_breech.DualCannonMountPoint;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.quick_firing_breech.ProjectileRackCannonMountPoint;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.quick_firing_breech.TorpedoCannonMountPoint;
import com.cubester.cbc_compact_mount.compat.CMArmInteractionPointTypes;
import com.cubester.cbc_compact_mount.content.CompactCannonMountBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.ExtendsCannonMount;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.CannonMountPoint;

@Mixin(
		value = {CMArmInteractionPointTypes.CompactCannonMountPoint.class},
		remap = false
)
public class CompatMountPointMixin {
	public CompatMountPointMixin() {
	}

	@Inject(
			method = {"insert"},
			at = {@At("RETURN")},
			cancellable = true
	)
	public void getInsertedResultAndDoSomethingInject(ArmBlockEntity armBlockEntity, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
		ArmInteractionPoint self = (ArmInteractionPoint)(Object)this;
		BlockEntity be = self.getLevel().getBlockEntity(self.getPos());
		PitchOrientedContraptionEntity poce = null;

		if (be instanceof CompactCannonMountBlockEntity mount) {
			poce = mount.getContraption();
		} else if (be instanceof ExtendsCannonMount extendsMount) {
			CannonMountBlockEntity base = extendsMount.getCannonMount();
			if (base != null)
				poce = base.getContraption();
		}

		if (poce == null || !(poce.getContraption() instanceof AbstractMountedCannonContraption cannon))
			return;
		if (cannon instanceof MountedTorpedoTubeContraption torp) {
			cir.setReturnValue(TorpedoCannonMountPoint.torpedoTubeInsert(stack, simulate, torp, poce));
		}else if (cannon instanceof MountedProjectileRackContraption rack) {
			cir.setReturnValue(ProjectileRackCannonMountPoint.projectileRackInsert(stack, simulate, rack, poce));
		}else if (cannon instanceof MountedDualCannonContraption dualCannon){
			cir.setReturnValue(DualCannonMountPoint.dualCannonInsert(stack, simulate, dualCannon, poce));
		}

	}
}