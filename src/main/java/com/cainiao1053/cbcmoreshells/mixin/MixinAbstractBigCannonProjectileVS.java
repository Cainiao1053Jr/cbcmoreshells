package com.cainiao1053.cbcmoreshells.mixin;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
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
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;

@Mixin(value = AbstractBigCannonProjectile.class, remap = false)
public abstract class MixinAbstractBigCannonProjectileVS {

    @Unique
    private Long vs$embeddedShipId = null;

    @Inject(method = "calculateBlockPenetration", at = @At("RETURN"), remap = false)
    private void cbcms$onPenetrationCalculated(
            ProjectileContext projectileContext,
            BlockState blockState,
            BlockHitResult blockHitResult,
            CallbackInfoReturnable<ImpactResult> cir) {
        if (vs$embeddedShipId != null) return;
        ImpactResult result = cir.getReturnValue();
        if (result.kinematics() == ImpactResult.KinematicOutcome.STOP) {
            AbstractBigCannonProjectile self = (AbstractBigCannonProjectile) (Object) this;
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
            AbstractBigCannonProjectile self = (AbstractBigCannonProjectile) (Object) this;
            if (!self.level().isClientSide) {
                ((IEntityDraggingInformationProvider) this)
                        .getDraggingInformation()
                        .setLastShipStoodOn(vs$embeddedShipId);
            }
        }
    }
}
