package com.cainiao1053.cbcmoreshells.munitions.fuzes;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeContainer;

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
		CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
		CompoundTag tag = cd != null ? cd.copyTag() : new CompoundTag();

		// Countdown after proximity activation.
		if (tag.getBoolean("Activated")) {
			int timer = tag.getInt("FuzeTimer");
			if (timer <= 0) return true;
			tag.putInt("FuzeTimer", timer - 1);
			stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
			return false;
		}

//		int airTime = tag.getInt("AirTime");
//		if (airTime > CBCConfigs.server().munitions.proximityFuzeArmingTime.get()) tag.putBoolean("Armed", true);
//		tag.putInt("AirTime", ++airTime);
//		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
//		return false;
		int airTime = stack.getOrDefault(CBCDataComponents.AIR_TIME, 0);
		if (airTime > CBCConfigs.server().munitions.proximityFuzeArmingTime.get()) stack.set(CBCDataComponents.ARMED, true);
		stack.set(CBCDataComponents.AIR_TIME, ++airTime);
		return false;
	}

	@Override
	public boolean canLingerInGround(ItemStack stack, AbstractCannonProjectile projectile) {
		CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
		return cd != null && cd.copyTag().getBoolean("Activated");
	}

	@Override
	public boolean onProjectileClip(ItemStack stack, AbstractCannonProjectile projectile, Vec3 start, Vec3 end, ProjectileContext ctx, boolean baseFuze) {
		if (baseFuze) return false;
		CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
		CompoundTag tag = cd != null ? cd.copyTag() : new CompoundTag();
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
//		var shipWorldCore = VSGameUtilsKt.getShipObjectWorld((ServerLevel) projectile.level());

//		for (var ship : shipWorldCore.getLoadedShips()) {
//			AABBdc shipABdc = ship.getWorldAABB();
//			AABB shipAABB = toAABB(shipABdc);
//			if (currentMovementRegion.intersects(shipAABB)) {
//				// Don't detonate immediately; start the fixed-tick countdown instead.
//				if(time > 1){
//					tag.putBoolean("Activated", true);
//					tag.putInt("FuzeTimer", Math.max(time - 2, 0));
//					return false;
//				}
//				return true;
//			}
//		}

		return super.onProjectileClip(stack, projectile, start, end, ctx, false);
	}

//	public static AABB toAABB(AABBdc i) {
//		return new AABB(
//				i.minX(), i.minY(), i.minZ(),
//				i.maxX(), i.maxY(), i.maxZ()
//		);
//	}

//	@Override
//	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//		if (player instanceof ServerPlayer splayer && player.mayBuild()) {
//			ItemStack stack = player.getItemInHand(hand);
//			CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
//			CompoundTag tag = cd != null ? cd.copyTag() : new CompoundTag();
//			if (!tag.contains("DetonationDistance")) {
//				tag.putInt("DetonationDistance", 1);
//				stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
//			}
//			int dist = tag.getInt("DetonationDistance");
//			CBCMenuTypes.SET_PROXIMITY_FUZE.open(splayer, this.getDisplayName(), this, buf -> {
//				buf.writeVarInt(dist);
//				buf.writeItem(new ItemStack(this));
//			});
//		}
//		return super.use(level, player, hand);
//	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer splayer && player.mayBuild()) {
			ItemStack stack = player.getItemInHand(hand);
			if (!stack.has(CBCDataComponents.DETONATION_DISTANCE)) {
				stack.set(CBCDataComponents.DETONATION_DISTANCE, 1);
			}
			int dist = stack.getOrDefault(CBCDataComponents.DETONATION_DISTANCE, 1);
			CBCMenuTypes.SET_PROXIMITY_FUZE.open(splayer, this.getDisplayName(), this, buf -> {
				buf.writeVarInt(dist);
				ItemStack.STREAM_CODEC.encode(buf, new ItemStack(this));
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
		stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> {
			CompoundTag t = data.copyTag();
			t.putInt("DetonationDistance", 1);
			return CustomData.of(t);
		});
		return stack;
	}

	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		CustomData addExtraCd = stack.get(DataComponents.CUSTOM_DATA);
		int detonDist = addExtraCd != null ? addExtraCd.copyTag().getInt("DetonationDistance") : 0;
		MutableComponent info = Lang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".proximity_fuze.tooltip.shell_info", detonDist)
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, Style.EMPTY, Style.EMPTY, 6));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		CBCMSTooltip.appendProxyFuzeInfo(stack, context, tooltip, flag);
		CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
		int detonDist = cd != null ? cd.copyTag().getInt("DetonationDistance") : 0;
		tooltip.add(Lang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".proximity_fuze.tooltip.shell_info.item", detonDist)
			.component());
	}

}
