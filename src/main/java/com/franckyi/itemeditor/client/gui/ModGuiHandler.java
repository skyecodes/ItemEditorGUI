package com.franckyi.itemeditor.client.gui;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.config.ModConfiguration;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

	public static final int ITEM_EDITOR_MENU = 0;
	public static final int ITEM_EDITOR_DISPLAY = 1;
	public static final int ITEM_EDITOR_LORE = 2;
	public static final int ITEM_EDITOR_ENCHANT = 3;
	public static final int ITEM_EDITOR_HIDEFLAGS = 4;
	public static final int ITEM_EDITOR_ATTRIBUTES = 5;
	public static final int ITEM_EDITOR_COMPACT = 6;

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case (ITEM_EDITOR_MENU):
			return new GuiModMenu();
		case (ITEM_EDITOR_DISPLAY):
			return new GuiEditDisplay(ITEM_EDITOR_MENU, ItemEditorMod.config.pauseGame, ItemEditorMod.instance);
		case (ITEM_EDITOR_LORE):
			return new GuiEditLore(ITEM_EDITOR_DISPLAY, ItemEditorMod.config.pauseGame, ItemEditorMod.instance);
		case (ITEM_EDITOR_ENCHANT):
			return new GuiEditEnchant(ITEM_EDITOR_MENU, ItemEditorMod.config.pauseGame, ItemEditorMod.instance);
		case (ITEM_EDITOR_HIDEFLAGS):
			return new GuiEditHideFlags(ITEM_EDITOR_DISPLAY, ItemEditorMod.config.pauseGame, ItemEditorMod.instance);
		case (ITEM_EDITOR_ATTRIBUTES):
			return new GuiEditAttributes(ITEM_EDITOR_MENU, ItemEditorMod.config.pauseGame, ItemEditorMod.instance);
		case (ITEM_EDITOR_COMPACT):
			int guiScale = new Integer(Minecraft.getMinecraft().gameSettings.guiScale);
			Minecraft.getMinecraft().gameSettings.guiScale = getScaleFromWindowSize();
			return new GuiCompactEditor(ITEM_EDITOR_MENU, guiScale, ItemEditorMod.config.pauseGame, ItemEditorMod.instance);
		default:
			return null;
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public static int getDefaultGui() {
		return (ItemEditorMod.config.preferCompactInterface) ? ITEM_EDITOR_COMPACT : ITEM_EDITOR_MENU;
	}

	private int getScaleFromWindowSize() {
		if (Minecraft.getMinecraft().displayWidth <= 1400 || Minecraft.getMinecraft().displayHeight <= 750)
			return 1;
		if (Minecraft.getMinecraft().displayWidth <= 2100 || Minecraft.getMinecraft().displayHeight <= 1150)
			return 2;
		if (Minecraft.getMinecraft().displayWidth > 2100 && Minecraft.getMinecraft().displayHeight > 1150)
			return 3;
		return 0;
	}

}
