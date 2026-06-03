package com.cainiao1053.cbcmoreshells.items.fire_extinguisher;


import com.cainiao1053.cbcmoreshells.CBCMSCompatTransformers;
import com.cainiao1053.cbcmoreshells.config.CBCMSCfgServer;
import com.cainiao1053.cbcmoreshells.config.CBCMSConfigs;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FireExtinguisherItem extends Item {

    private static final String TAG_ANCHOR = "boundAnchor";

    public FireExtinguisherItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(stack.getItem())) {
            return InteractionResultHolder.pass(stack);
        }

        Vec3 start = player.getEyePosition();
        Vec3 viewVector = player.getViewVector(1.0F);
        Vec3 end = start.add(viewVector.scale(CBCMSConfigs.server().extinguisherDistance.get()));

        for (int i = 0; i < 50; i++) {
            world.addParticle(
                    ParticleTypes.CLOUD,
                    start.x,
                    start.y,
                    start.z,
                    viewVector.x * 0.6,
                    viewVector.y * 0.6,
                    viewVector.z * 0.6
            );
        }

        if (world.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        BlockHitResult hit = world.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

        if (hit.getType() != BlockHitResult.Type.BLOCK) {
            return InteractionResultHolder.success(stack);
        }

        BlockPos hitPos = hit.getBlockPos();
        Vec3 hitVec = new Vec3(hitPos.getX(), hitPos.getY(), hitPos.getZ());
        AABB hitBox = AABB.ofSize(hitVec,1,1,1);
        CBCMSCompatTransformers.extinguishFireOnSublevel(world, hitBox, CBCMSConfigs.server().extinguisherRange.get(), hitVec);

        world.playSound(null, hitPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 1.4F);
        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        player.getCooldowns().addCooldown(stack.getItem(), CBCMSConfigs.server().extinguisherCooldown.get());

        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        return false;
    }

}
