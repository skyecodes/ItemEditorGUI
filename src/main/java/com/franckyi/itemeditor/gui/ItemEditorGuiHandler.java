package com.franckyi.itemeditor.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ItemEditorGuiHandler implements IGuiHandler {

	public static final int ITEM_EDITOR_MENU = 0;
	public static final int ITEM_EDITOR_DISPLAY = 1;
	public static final int ITEM_EDITOR_LORE = 2;
	public static final int ITEM_EDITOR_ENCHANT = 3;

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case (ITEM_EDITOR_MENU):
			return new GuiItemEditorMenu();
		case (ITEM_EDITOR_DISPLAY):
			return new GuiItemEditorDisplay();
		case (ITEM_EDITOR_LORE):
			return new GuiItemEditorLore();
		case (ITEM_EDITOR_ENCHANT):
			return new GuiItemEditorEnchant();
		default:
			return null;
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

}
