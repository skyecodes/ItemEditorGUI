package com.franckyi.itemeditor.event;

import org.lwjgl.input.Keyboard;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.ModReference;
import com.franckyi.itemeditor.client.gui.ModGuiHandler;
import com.franckyi.itemeditor.helper.ModHelper;
import com.franckyi.itemeditor.network.GetClientStackMessage;
import com.franckyi.itemeditor.network.ModPacketHandler;
import com.franckyi.itemeditor.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyPressed(KeyboardInputEvent.Post e) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (Keyboard.getEventKey() == ClientProxy.keyBinding.getKeyCode() && Keyboard.getEventKeyState() == true)
			if (e.getGui() instanceof GuiContainer
					&& (Minecraft.getMinecraft().playerController.getCurrentGameType().equals(GameType.CREATIVE)
							|| !ItemEditorMod.config.creativeModeOnly)) {
				GuiContainer inventoryScreen = ((GuiContainer) e.getGui());
				if (inventoryScreen.getSlotUnderMouse() != null)
					if (inventoryScreen.getSlotUnderMouse().getHasStack())
						if (inventoryScreen.getSlotUnderMouse().inventory.equals(player.inventory)) {
							ModHelper.clientStack = ((GuiContainer) e.getGui()).getSlotUnderMouse().getStack();
							ModPacketHandler.INSTANCE.sendToServer(
									new GetClientStackMessage(player.inventory.getSlotFor(ModHelper.clientStack)));
							player.openGui(ItemEditorMod.instance, ModGuiHandler.getDefaultGui(),
									Minecraft.getMinecraft().world, (int) player.posX, (int) player.posY,
									(int) player.posZ);
						} else
							player.sendMessage(new TextComponentString(TextFormatting.RED + "[" + ModReference.NAME
									+ "] You select an item in your inventory."));
			} else
				player.sendMessage(new TextComponentString(
						TextFormatting.RED + "[" + ModReference.NAME + "] You must be in Creative mode."));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) {
		if (ClientProxy.keyBinding.isPressed() && Minecraft.getMinecraft().currentScreen == null) {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			if (!player.getHeldItemMainhand().getItem().equals(Item.getItemFromBlock(Blocks.AIR))
					&& (Minecraft.getMinecraft().playerController.getCurrentGameType().equals(GameType.CREATIVE)
							|| !ItemEditorMod.config.creativeModeOnly)) {
				ModHelper.clientStack = player.getHeldItemMainhand();
				ModPacketHandler.INSTANCE
						.sendToServer(new GetClientStackMessage(player.inventory.getSlotFor(ModHelper.clientStack)));
				player.openGui(ItemEditorMod.instance, ModGuiHandler.getDefaultGui(), Minecraft.getMinecraft().world,
						(int) player.posX, (int) player.posY, (int) player.posZ);
			}
			if (player.getHeldItemMainhand().getItem().equals(Item.getItemFromBlock(Blocks.AIR)))
				player.sendMessage(new TextComponentString(
						TextFormatting.RED + "[" + ModReference.NAME + "] You must hold an item in your hand."));
			if (!Minecraft.getMinecraft().playerController.getCurrentGameType().equals(GameType.CREATIVE))
				player.sendMessage(new TextComponentString(
						TextFormatting.RED + "[" + ModReference.NAME + "] You must be in Creative mode."));
		}
	}

}
