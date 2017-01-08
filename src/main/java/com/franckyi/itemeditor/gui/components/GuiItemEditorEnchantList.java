package com.franckyi.itemeditor.gui.components;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.misc.ModEnchantmentHelper.EnchantmentListEntry;
import com.franckyi.itemeditor.misc.ModEnchantmentHelper.EnumEnchantmentList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.item.Item;

public class GuiItemEditorEnchantList extends GuiListExtended{
	
	private List<EnchantmentListEntry> enchList;

	public GuiItemEditorEnchantList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn,
			int slotHeightIn) {
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		enchList = new ArrayList<EnchantmentListEntry>();
		enchList.addAll(EnumEnchantmentList.getDefaults(widthIn, heightIn));
		if(ItemEditorMod.config.safeEnchantmentsFirst){
			enchList.sort(new Comparator<EnchantmentListEntry>(){
				@Override
				public int compare(EnchantmentListEntry ench1, EnchantmentListEntry ench2) {
					Item item = mc.player.getHeldItemMainhand().getItem();
					if(ench1.getEnch().getType().canEnchantItem(item) && !ench2.getEnch().getType().canEnchantItem(item))
						return -1;
					if(!ench1.getEnch().getType().canEnchantItem(item) && ench2.getEnch().getType().canEnchantItem(item))
						return 1;
					return 0;
				}
			});
		}
		if(ItemEditorMod.config.globalEnchantmentsLast){
			enchList.sort(new Comparator<EnchantmentListEntry>(){
				@Override
				public int compare(EnchantmentListEntry ench1, EnchantmentListEntry ench2) {
					if(!(ench1.getEnch().getId() == 10 || ench1.getEnch().getId() == 71) && (ench2.getEnch().getId() == 10 || ench2.getEnch().getId() == 71))
						return -1;
					if((ench1.getEnch().getId() == 10 || ench1.getEnch().getId() == 71) && !(ench2.getEnch().getId() == 10 || ench2.getEnch().getId() == 71))
						return 1;
					return 0;
				}
			});
		}
	}

	@Override
	public IGuiListEntry getListEntry(int index) {
		return enchList.get(index);
	}

	@Override
	protected int getSize() {
		return enchList.size();
	}

	public List<EnchantmentListEntry> getEnchantmentList() {
		return enchList;
	}

}
