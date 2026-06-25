package com.cainiao1053.cbcmoreshells.items.cannon_combo;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class TorpedoTubeComboItem extends CannonComboItem {

	public TorpedoTubeComboItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if(level.isClientSide()){
			return InteractionResultHolder.pass(player.getItemInHand(hand));
		}
		ItemStack stack = player.getItemInHand(hand);
		ServerPlayer sp = (ServerPlayer) player;
		giveOrDrop(sp, new ItemStack(CBCMSBlocks.STEEL_TORPEDO_BARREL, 4));
		giveOrDrop(sp, new ItemStack(CBCMSBlocks.STEEL_TORPEDO_CHAMBER, 2));
		giveOrDrop(sp, new ItemStack(CBCMSBlocks.STEEL_TORPEDO_QUICKFIRING_BREECH, 1));
		stack.shrink(1);
		return InteractionResultHolder.success(stack);
	}
}



