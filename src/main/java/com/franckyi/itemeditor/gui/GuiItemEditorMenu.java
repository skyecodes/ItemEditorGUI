package com.franckyi.itemeditor.gui;

import java.io.IOException;

import com.franckyi.itemeditor.ItemEditorMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiItemEditorMenu extends GuiScreen {

	private GuiButton displayButton;
	private GuiButton enchantmentsButton;
	private GuiButton customEffectsButton;
	private GuiButton exitButton;

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.exitButton) {
			this.mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null)
				this.mc.setIngameFocus();
		}
		if (button == this.displayButton)
			switchGui(ItemEditorGuiHandler.ITEM_EDITOR_DISPLAY);
		if (button == this.enchantmentsButton)
			switchGui(ItemEditorGuiHandler.ITEM_EDITOR_ENCHANT);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		this.drawString(fontRendererObj, "Item Editor Menu", this.width / 2 - 40, this.height / 2 - 90, 0xffffff);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		this.buttonList
				.add(this.displayButton = new GuiButton(0, this.width / 2 - 100, this.height / 2 - 55, "Display"));
		this.buttonList.add(this.enchantmentsButton = new GuiButton(1, this.width / 2 - 100, this.height / 2 - 25,
				"Enchantments"));
		this.buttonList.add(this.customEffectsButton = new GuiButton(2, this.width / 2 - 100, this.height / 2 + 5,
				"Custom Effects [SOON]"));
		this.buttonList.add(this.exitButton = new GuiButton(3, this.width / 2 - 50, this.height / 2 + 35, 100, 20, "Exit Menu"));
		this.buttonList.get(2).enabled = false;
	}
	
	private void switchGui(int id){
		mc.player.openGui(ItemEditorMod.instance, id, mc.world,
				(int) mc.player.posX, (int) mc.player.posY,
				(int) mc.player.posZ);
	}

}
