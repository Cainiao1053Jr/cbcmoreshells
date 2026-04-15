package com.cainiao1053.cbcmoreshells.munitions.fuzes;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.primitives.AABBdc;
import org.slf4j.Logger;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeContainer;

import javax.annotation.Nullable;
import java.util.List;

public class ShipProximityFuzeItem extends FuzeItem implements MenuProvider {

	public ShipProximityFuzeItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile projectile, HitResult hitResult, AbstractCannonProjectile.ImpactResult impactResult, boolean baseFuze) {
		return !baseFuze;
	}

	Logger LOGGER = Cbcmoreshells.LOGGER;

	@Override
	public boolean onProjectileExpiry(ItemStack stack, AbstractCannonProjectile projectile) {
		return true;
	}

	@Override
	public boolean onProjectileTick(ItemStack stack, AbstractCannonProjectile projectile) {
		CompoundTag tag = stack.getOrCreateTag();

		// Countdown after proximity activation.
		if (tag.getBoolean("Activated")) {
			int timer = tag.getInt("FuzeTimer");
			if (timer <= 0) return true;
			tag.putInt("FuzeTimer", timer - 1);
			return false;
		}

		int airTime = tag.getInt("AirTime");
		if (airTime > CBCConfigs.SERVER.munitions.proximityFuzeArmingTime.get()) tag.putBoolean("Armed", true);
		tag.putInt("AirTime", ++airTime);
		return false;
	}

	@Override
	public boolean canLingerInGround(ItemStack stack, AbstractCannonProjectile projectile) {
		return stack.getOrCreateTag().getBoolean("Activated");
	}

	@Override
	public boolean onProjectileClip(ItemStack stack, AbstractCannonProjectile projectile, Vec3 start, Vec3 end, ProjectileContext ctx, boolean baseFuze) {
		if (baseFuze) return false;
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains("Armed")) return false;

		int time = Math.max(tag.getInt("DetonationDistance"), 1);
		Vec3 dir = projectile.getOrientation().normalize();
		//Vec3 right = dir.cross(new Vec3(Direction.UP.step()));
		//Vec3 up = dir.cross(right);
		dir = dir.scale(2);
		//double reach = Math.max(projectile.getBbWidth(), projectile.getBbHeight()) * 0.5;

		// Sweep the AABB along this tick's displacement so fast-moving projectiles
		// don't tunnel through ships between ticks.
		Vec3 tickDisplacement = end.subtract(start);
		AABB currentMovementRegion = projectile.getBoundingBox()
			.expandTowards(dir.scale(1.75))
			.inflate(4)
			.move(start.subtract(projectile.position()))
			.expandTowards(tickDisplacement);

		//List<Entity> entities = projectile.level().getEntities(projectile, currentMovementRegion, projectile::canHitEntity);
		var shipWorldCore = VSGameUtilsKt.getShipObjectWorld((ServerLevel) projectile.level());

		for (var ship : shipWorldCore.getLoadedShips()) {
			AABBdc shipABdc = ship.getWorldAABB();
			AABB shipAABB = toAABB(shipABdc);
			if (currentMovementRegion.intersects(shipAABB)) {
				// Don't detonate immediately; start the fixed-tick countdown instead.
				if(time > 1){
					tag.putBoolean("Activated", true);
					tag.putInt("FuzeTimer", Math.max(time - 2, 0));
					return false;
				}
				return true;
			}
		}
//		int radius = CBCConfigs.SERVER.munitions.proximityFuzeScale.get();
//		double scale = CBCConfigs.SERVER.munitions.proximityFuzeSpacing.get();
//		for (int i = -radius; i <= radius; ++i) {
//			for (int j = -radius; j <= radius; ++j) {
//				Vec3 ray = dir.add(right.scale(i * scale)).add(up.scale(j * scale));
//				Vec3 rayEnd = start.add(ray);
//
//				if (projectile.level().clip(new ClipContext(start, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, projectile)).getType() != HitResult.Type.MISS) {
//					return true;
//				}
//
//				for (Entity target : entities) {
//					AABB targetBox = target.getBoundingBox().inflate(reach);
//					if (targetBox.clip(start, rayEnd).isPresent())
//						return true;
//				}
//			}
//		}

		return super.onProjectileClip(stack, projectile, start, end, ctx, false);
	}

	public static AABB toAABB(AABBdc i) {
		return new AABB(
				i.minX(), i.minY(), i.minZ(),
				i.maxX(), i.maxY(), i.maxZ()
		);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer splayer && player.mayBuild()) {
			ItemStack stack = player.getItemInHand(hand);
			CompoundTag tag = stack.getOrCreateTag();
			if (!tag.contains("DetonationDistance")) {
				tag.putInt("DetonationDistance", 1);
			}
			int dist = tag.getInt("DetonationDistance");
			CBCMenuTypes.SET_PROXIMITY_FUZE.open(splayer, this.getDisplayName(), this, buf -> {
				buf.writeVarInt(dist);
				buf.writeItem(new ItemStack(this));
			});
		}
		return super.use(level, player, hand);
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
		return ProximityFuzeContainer.getServerMenu(windowId, playerInv, player.getMainHandItem());
	}

	@Override
	public Component getDisplayName() {
		return this.getDescription();
	}

	public static ItemStack getCreativeTabItem(int defaultFuze) {
		ItemStack stack = CBCItems.PROXIMITY_FUZE.asStack();
		stack.getOrCreateTag().putInt("DetonationDistance", 1);
		return stack;
	}

	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		MutableComponent info = Lang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".proximity_fuze.tooltip.shell_info", stack.getOrCreateTag().getInt("DetonationDistance"))
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, Style.EMPTY, Style.EMPTY, 6));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CBCMSTooltip.appendProxyFuzeInfo(stack,level,tooltip,flag);
		tooltip.add(Lang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".proximity_fuze.tooltip.shell_info.item", stack.getOrCreateTag().getInt("DetonationDistance"))
			.component());
	}

}
