package com.franckyi.itemeditor.gui.components;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.misc.ModEnchantmentHelper.EnchantmentListEntry;
import com.franckyi.itemeditor.misc.ModEnchantmentHelper.EnumEnchantmentList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiItemEditorEnchantList extends GuiScrollingList {

	private GuiScreen parent;
	private List<EnchantmentListEntry> enchList;

	public GuiItemEditorEnchantList(final Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int leftIn,
			int slotHeightIn, int screenWidthIn, int screenHeightIn, GuiScreen parent) {
		super(mcIn, widthIn, heightIn, topIn, bottomIn, leftIn, slotHeightIn, screenWidthIn, screenHeightIn);
		this.parent = parent;
		enchList = new ArrayList<EnchantmentListEntry>();
		enchList.addAll(EnumEnchantmentList.getDefaults(widthIn, heightIn));
		if (ItemEditorMod.config.safeEnchantmentsFirst) {
			enchList.sort(new Comparator<EnchantmentListEntry>() {
				@Override
				public int compare(EnchantmentListEntry ench1, EnchantmentListEntry ench2) {
					Item item = mcIn.player.getHeldItemMainhand().getItem();
					if (ench1.getEnch().getType().canEnchantItem(item)
							&& !ench2.getEnch().getType().canEnchantItem(item))
						return -1;
					if (!ench1.getEnch().getType().canEnchantItem(item)
							&& ench2.getEnch().getType().canEnchantItem(item))
						return 1;
					return 0;
				}
			});
		}
		if (ItemEditorMod.config.globalEnchantmentsLast) {
			enchList.sort(new Comparator<EnchantmentListEntry>() {
				@Override
				public int compare(EnchantmentListEntry ench1, EnchantmentListEntry ench2) {
					if (!(ench1.getEnch().getId() == 10 || ench1.getEnch().getId() == 71)
							&& (ench2.getEnch().getId() == 10 || ench2.getEnch().getId() == 71))
						return -1;
					if ((ench1.getEnch().getId() == 10 || ench1.getEnch().getId() == 71)
							&& !(ench2.getEnch().getId() == 10 || ench2.getEnch().getId() == 71))
						return 1;
					return 0;
				}
			});
		}
	}

	@Override
	protected int getSize() {
		return enchList.size();
	}

	public List<EnchantmentListEntry> getEnchantmentList() {
		return enchList;
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		enchList.get(index).getTextField().setFocused(true);
	}

	@Override
	protected boolean isSelected(int index) {
		return false;
	}

	@Override
	protected void drawBackground() {
	}

	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		enchList.get(slotIdx).getTextField().yPosition = slotTop;
		enchList.get(slotIdx).getTextField().xPosition = listWidth + listWidth/6;
		enchList.get(slotIdx).getTextField().drawTextBox();
		if (enchList.get(slotIdx).getEnch().getId() == 10 || enchList.get(slotIdx).getEnch().getId() == 71) {
			parent.drawString(Minecraft.getMinecraft().fontRendererObj, enchList.get(slotIdx).getEnch().getName(),
					left + 10, slotTop + 5, 0xaa0000);
		} else if (enchList.get(slotIdx).getEnch().getType().canEnchantItem(Minecraft.getMinecraft().player.getHeldItemMainhand().getItem())) {
			parent.drawString(Minecraft.getMinecraft().fontRendererObj, enchList.get(slotIdx).getEnch().getName(),
					left + 10, slotTop + 5, 0x00aa00);
		} else {
			parent.drawString(Minecraft.getMinecraft().fontRendererObj, enchList.get(slotIdx).getEnch().getName(),
					left + 10, slotTop + 5, 0xffffff);
		}
	}

}
