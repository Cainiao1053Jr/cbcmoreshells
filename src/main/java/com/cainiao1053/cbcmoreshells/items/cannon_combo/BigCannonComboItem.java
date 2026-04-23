//package com.cainiao1053.cbcmoreshells.items.cannon_combo;
//
//import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
//import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
//import net.minecraft.core.RegistryAccess;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.TooltipFlag;
//import net.minecraft.world.level.Level;
//import org.jetbrains.annotations.Nullable;
//import rbasamoyai.createbigcannons.CreateBigCannons;
//
//import java.util.List;
//
//
//public class BigCannonComboItem extends Item {
//
//	public BigCannonComboItem(Properties properties) {
//		super(properties);
//	}
//
//	private static final String BARREL = "barrel";
//	private static final String CHAMBER = "chamber";
//	private static final String QFB = "_quickfiring_breech";
//	private static final String BUILD_UP = "built_up_";
//
//	@Override
//	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//		if(level.isClientSide()){
//			return InteractionResultHolder.pass(player.getItemInHand(hand));
//		}
//		ItemStack stack = player.getItemInHand(hand);
//		CompoundTag tag = stack.getOrCreateTag();
//		String material = tag.getString("Material");
//		if(material.isEmpty()){
//			return InteractionResultHolder.fail(stack);
//		}
//		String prefix = CreateBigCannons.MOD_ID + ":" + material;
//		String builtUpPrefix = CreateBigCannons.MOD_ID + ":" + BUILD_UP + material;
//		String barrelId = prefix + getCanonType() + BARREL;
//		String builtUpBarrelId = builtUpPrefix + getCanonType() + BARREL;
//		String chamberId = prefix + getCanonType() + CHAMBER;
//		String qfbId = prefix + QFB;
//		if(material.equals("nethersteel")){
//			qfbId = Cbcmoreshells.MODID + ":" + "nethersteel" + QFB;
//		}
//		RegistryAccess ra = level.registryAccess();
//		ItemStack barrel = stackFromItemId(ra, barrelId, 6);
//		ItemStack chamber = stackFromItemId(ra, chamberId, 2);
//		ItemStack builtUpBarrel = stackFromItemId(ra, builtUpBarrelId, 2);
//		ItemStack qfb = stackFromItemId(ra, qfbId, 1);
//		ServerPlayer sp = (ServerPlayer) player;
//		giveOrDrop(sp, barrel);
//		giveOrDrop(sp, chamber);
//		giveOrDrop(sp, builtUpBarrel);
//		giveOrDrop(sp, qfb);
//		stack.shrink(1);
//		return InteractionResultHolder.success(stack);
//	}
//
//	protected String getCanonType(){
//		return "_cannon_";
//	}
//
//	@Override
//	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
//		super.appendHoverText(stack, context, tooltip, flag);
//		CBCMSTooltip.appendCannonComboInfo(stack, context, tooltip, flag);
//	}
//
//	public static ItemStack stackFromItemId(RegistryAccess access, String id, int count) {
//		ResourceLocation rl = ResourceLocation.tryParse(id);
//		if (rl == null) return ItemStack.EMPTY;
//
//		Item item = access.registryOrThrow(Registries.ITEM).get(rl);
//		if (item == null) return ItemStack.EMPTY;
//		return new ItemStack(item, Math.max(1, count));
//	}
//
//	public static void giveOrDrop(ServerPlayer player, ItemStack stack) {
//		if (player == null || stack == null || stack.isEmpty()) return;
//		Level level = player.level();
//		if (level.isClientSide) return;
//		while (!stack.isEmpty()) {
//			int toMove = Math.min(stack.getCount(), stack.getMaxStackSize());
//			ItemStack part = stack.copy();
//			part.setCount(toMove);
//
//			boolean inserted = player.getInventory().add(part);
//
//			if (inserted) {
//				stack.shrink(toMove);
//			} else {
//				dropAtPlayer(player, part);
//				stack.shrink(toMove);
//			}
//		}
//
//		player.getInventory().setChanged();
//		player.inventoryMenu.broadcastChanges();
//	}
//
//	private static void dropAtPlayer(ServerPlayer player, ItemStack stackToDrop) {
//		if (stackToDrop.isEmpty()) return;
//		Level level = player.level();
//		ItemEntity itemEntity = new ItemEntity(
//				level,
//				player.getX(), player.getY() + 0.5, player.getZ(),
//				stackToDrop
//		);
//		itemEntity.setDeltaMovement(
//				level.random.nextGaussian() * 0.05,
//				0.1,
//				level.random.nextGaussian() * 0.05
//		);
//		level.addFreshEntity(itemEntity);
//	}
//}
//
//
//
