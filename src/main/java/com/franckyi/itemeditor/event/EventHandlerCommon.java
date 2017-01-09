package com.franckyi.itemeditor.event;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.ModReference;
import com.franckyi.itemeditor.gui.ItemEditorGuiHandler;
import com.franckyi.itemeditor.misc.SharedContent;
import com.franckyi.itemeditor.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandlerCommon {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyPressed(KeyInputEvent e) {
		KeyBinding[] keyBindings = ClientProxy.keyBindings;
		if (keyBindings[0].isPressed()) {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			if (!player.getHeldItemMainhand().getItem().equals(Item.getItemFromBlock(Blocks.AIR))
					&& Minecraft.getMinecraft().playerController.getCurrentGameType().equals(GameType.CREATIVE)) {
				player.openGui(ItemEditorMod.instance, ItemEditorGuiHandler.ITEM_EDITOR_MENU,
						Minecraft.getMinecraft().world, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
			if (player.getHeldItemMainhand().getItem().equals(Item.getItemFromBlock(Blocks.AIR))) {
				player.sendChatMessage(TextFormatting.RED + "[ItemEditor] Please hold an item in your hand.");
			}
			if (!Minecraft.getMinecraft().playerController.getCurrentGameType().equals(GameType.CREATIVE)) {
				player.sendChatMessage(TextFormatting.RED + "[ItemEditor] You have to be in Creative mode.");
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent e) {
		if (e.getGui() == null) {
			SharedContent.clear();
		}
	}

}
