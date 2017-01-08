package com.franckyi.itemeditor.gui.components;

import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.misc.ModEnchantmentHelper.EnumEnchantmentList;
import com.franckyi.itemeditor.misc.ModEnchantmentHelper.EnchantmentListEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;

public class GuiItemEditorEnchantList extends GuiListExtended{
	
	private List<EnchantmentListEntry> enchList;

	public GuiItemEditorEnchantList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn,
			int slotHeightIn) {
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		enchList = new ArrayList<EnchantmentListEntry>();
		enchList.addAll(EnumEnchantmentList.getDefaults(widthIn, heightIn));
	}

	@Override
	public IGuiListEntry getListEntry(int index) {
		return enchList.get(index);
	}

	@Override
	protected int getSize() {
		return 30;
	}

	public List<EnchantmentListEntry> getEnchantmentList() {
		return enchList;
	}

}
