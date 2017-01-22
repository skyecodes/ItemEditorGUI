package com.franckyi.itemeditor.client.gui.child;

import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.api.gui.GuiFormatButton;
import com.franckyi.itemeditor.helper.ModHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiLoreList extends GuiScrollingList {
	
	private GuiScreen parent;
	private List<LoreListEntry> loreList = new ArrayList<LoreListEntry>();

	public GuiLoreList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight,
			int screenWidth, int screenHeight, GuiScreen parent) {
		super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
		this.parent = parent;
		NBTTagList lores = ModHelper.clientStack.getOrCreateSubCompound("display").getTagList("Lore", NBT.TAG_STRING);
		for(int i = 0; i < ItemEditorMod.config.loreLineNumber; i++){
			loreList.add(new LoreListEntry(i));
			if(i < lores.tagCount())	
				loreList.get(i).textField.setText((lores.getStringTagAt(i).startsWith("§r")) ? lores.getStringTagAt(i).substring(2) : lores.getStringTagAt(i));
		}
	}

	@Override
	protected int getSize() {
		return loreList.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {	}

	@Override
	protected boolean isSelected(int index) {
		return false;
	}

	@Override
	protected void drawBackground() { }

	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		LoreListEntry entry = loreList.get(slotIdx);
		parent.drawString(parent.mc.fontRendererObj, "Line " + (entry.index + 1), left + 10, slotTop + 7, 0xffffff);
		entry.textField.xPosition = left + 50;
		entry.textField.yPosition = slotTop;
		entry.textField.width = listWidth - 100;
		entry.textField.drawTextBox();
		entry.formatButton.xPosition = entryRight - 30;
		entry.formatButton.yPosition = slotTop;
		entry.formatButton.visible = (entry.formatButton.yPosition > this.top && entry.formatButton.yPosition + 20 < this.bottom);
		entry.formatButton.enabled = entry.formatButton.visible;
	}
	
	public List<LoreListEntry> getLoreList(){
		return loreList;
	}
	
	public class LoreListEntry {
		
		private int index;
		private GuiTextField textField;
		private GuiFormatButton formatButton;
		
		public LoreListEntry(int index){
			this.index = index;
			this.textField = new GuiTextField((index+1)*10, parent.mc.fontRendererObj, 0, 0, 50, 20);
			this.formatButton = new GuiFormatButton((index+1)*20, 0, 0, textField, 1);
			this.formatButton.enabled = false;
			this.formatButton.visible = false;
			this.textField.mouseClicked(10, 45, 0);
			this.textField.setFocused(false);
		}

		public GuiTextField getTextField() {
			return textField;
		}

		public GuiButton getFormatButton() {
			return formatButton;
		}
		
	}

}
