package com.cainiao1053.cbcmoreshells.items.ballistic_journal;

import com.cainiao1053.cbcmoreshells.client.gui.CBCMSScreens;

import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Opens the dual cannon firing tables on right click. Purely a reference book — it holds no state
 * and changes nothing about the world, so the whole interaction lives on the client.
 */
public class BallisticJournalItem extends Item {

	public BallisticJournalItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide) return InteractionResultHolder.success(stack);

		CatnipServices.PLATFORM.executeOnClientOnly(() -> CBCMSScreens::openBallisticJournal);
		return InteractionResultHolder.success(stack);
	}

}
