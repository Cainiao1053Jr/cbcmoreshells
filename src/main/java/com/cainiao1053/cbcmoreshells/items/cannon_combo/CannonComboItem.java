package com.cainiao1053.cbcmoreshells.items.cannon_combo;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;


public class CannonComboItem extends Item {

	public CannonComboItem(Properties properties) {
		super(properties);
	}

	private static final String BARREL = "barrel";
	private static final String CHAMBER = "chamber";
	private static final String QFB = "quickfiring_breech";

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if(level.isClientSide()){
			return InteractionResultHolder.pass(stack);
		}
		String material = getMaterial(stack);
		if(material.isEmpty()){
			return InteractionResultHolder.fail(stack);
		}
		String prefix = Cbcmoreshells.MODID + ":" + material + getCanonType();
		ItemStack barrel = stackFromItemId(prefix + BARREL, 4);
		ItemStack chamber = stackFromItemId(prefix + CHAMBER, 2);
		ItemStack qfb = stackFromItemId(prefix + QFB, 1);
		if(barrel.isEmpty() && chamber.isEmpty() && qfb.isEmpty()){
			return InteractionResultHolder.fail(stack);
		}
		ServerPlayer sp = (ServerPlayer) player;
		giveOrDrop(sp, barrel);
		giveOrDrop(sp, chamber);
		giveOrDrop(sp, qfb);
		stack.shrink(1);
		return InteractionResultHolder.success(stack);
	}

	protected String getCanonType(){
		return "_dual_cannon_";
	}

	public static String getMaterial(ItemStack stack) {
		CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
		if (cd == null) return "";
		String material = cd.copyTag().getString("Material");
		int i = material.indexOf(':');
		return i >= 0 ? material.substring(i + 1) : material;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		CBCMSTooltip.appendCannonComboInfo(stack, context, tooltip, flag);
	}

	public static ItemStack stackFromItemId(String id, int count) {
		ResourceLocation rl = ResourceLocation.tryParse(id);
		if (rl == null) return ItemStack.EMPTY;

		return BuiltInRegistries.ITEM.getOptional(rl)
				.map(item -> new ItemStack(item, Math.max(1, count)))
				.orElse(ItemStack.EMPTY);
	}

	public static void giveOrDrop(ServerPlayer player, ItemStack stack) {
		if (player == null || stack == null || stack.isEmpty()) return;
		Level level = player.level();
		if (level.isClientSide) return;
		while (!stack.isEmpty()) {
			int toMove = Math.min(stack.getCount(), stack.getMaxStackSize());
			ItemStack part = stack.copyWithCount(toMove);
			stack.shrink(toMove);

			// add() shrinks part by whatever fit, the remainder is dropped
			if (!player.getInventory().add(part) || !part.isEmpty()) {
				dropAtPlayer(player, part);
			}
		}

		player.getInventory().setChanged();
		player.inventoryMenu.broadcastChanges();
	}

	private static void dropAtPlayer(ServerPlayer player, ItemStack stackToDrop) {
		if (stackToDrop.isEmpty()) return;
		Level level = player.level();
		ItemEntity itemEntity = new ItemEntity(
				level,
				player.getX(), player.getY() + 0.5, player.getZ(),
				stackToDrop
		);
		itemEntity.setDeltaMovement(
				level.random.nextGaussian() * 0.05,
				0.1,
				level.random.nextGaussian() * 0.05
		);
		level.addFreshEntity(itemEntity);
	}
}
