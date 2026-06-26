package com.cainiao1053.cbcmoreshells.mixin;

import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.AbstractDualCannonProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.LoadedShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.IEntityDraggingInformationProvider;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile.ImpactResult;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;

@Mixin(value = AbstractDualCannonProjectile.class, remap = false)
public abstract class MixinAbstractCannonProjectileVS {

    @Unique
    private Long vs$embeddedShipId = null;

    @Inject(method = "calculateBlockPenetration", at = @At("HEAD"), cancellable = true, remap = false)
    private void cbcms$skipPenetrationIfEmbedded(
            ProjectileContext projectileContext,
            BlockState blockState,
            BlockHitResult blockHitResult,
            CallbackInfoReturnable<ImpactResult> cir) {
        if (vs$embeddedShipId != null) {
            cir.setReturnValue(new ImpactResult(ImpactResult.KinematicOutcome.STOP, false));
        }
    }


    @Inject(method = "calculateBlockPenetration", at = @At("RETURN"), remap = false)
    private void cbcms$onPenetrationCalculated(
            ProjectileContext projectileContext,
            BlockState blockState,
            BlockHitResult blockHitResult,
            CallbackInfoReturnable<ImpactResult> cir) {
        ImpactResult result = cir.getReturnValue();
        if (result.kinematics() == ImpactResult.KinematicOutcome.STOP) {
            AbstractDualCannonProjectile self = (AbstractDualCannonProjectile) (Object) this;
            if (!self.level().isClientSide) {
                BlockPos blockPos = blockHitResult.getBlockPos();
                LoadedShip ship = VSGameUtilsKt.getShipObjectManagingPos(self.level(), blockPos);
                if (ship != null) {
                    vs$embeddedShipId = ship.getId();
                }
            }
        }
    }


    @Inject(method = "tick", at = @At("RETURN"), remap = true)
    private void cbcms$onTickReturn(CallbackInfo ci) {
        if (vs$embeddedShipId != null) {
            AbstractDualCannonProjectile self = (AbstractDualCannonProjectile) (Object) this;
            if (self.isInGround() && !self.level().isClientSide) {
                ((IEntityDraggingInformationProvider) this)
                        .getDraggingInformation()
                        .setLastShipStoodOn(vs$embeddedShipId);
            }
        }
    }
}
