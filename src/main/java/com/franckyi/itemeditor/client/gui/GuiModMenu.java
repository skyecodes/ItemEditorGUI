package com.franckyi.itemeditor.client.gui;

import java.io.IOException;

import com.franckyi.itemeditor.ItemEditorMod;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiModMenu extends GuiScreen {

	private GuiButton displayButton, enchantmentsButton, attributesButton, exitButton, compactButton;

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.exitButton) {
			this.mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null)
				this.mc.setIngameFocus();
		}
		if (button == this.displayButton)
			switchGui(ModGuiHandler.ITEM_EDITOR_DISPLAY);
		if (button == this.enchantmentsButton)
			switchGui(ModGuiHandler.ITEM_EDITOR_ENCHANT);
		if (button == this.attributesButton)
			switchGui(ModGuiHandler.ITEM_EDITOR_ATTRIBUTES);
		if (button == this.compactButton)
			switchGui(ModGuiHandler.ITEM_EDITOR_COMPACT);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return ItemEditorMod.config.pauseGame;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		this.drawString(fontRendererObj, "Item Editor Menu", this.width / 2 - 40, this.height / 2 - 90, 0x5555ff);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		this.buttonList
				.add(this.displayButton = new GuiButton(0, this.width / 2 - 100, this.height / 2 - 55, "Display"));
		this.buttonList.add(
				this.enchantmentsButton = new GuiButton(1, this.width / 2 - 100, this.height / 2 - 25, "Enchantments"));
		this.buttonList.add(this.attributesButton = new GuiButton(2, this.width / 2 - 100, this.height / 2 + 5,
				"Attributes"));
		this.buttonList.add(
				this.exitButton = new GuiButton(3, this.width / 2 - 50, this.height / 2 + 35, 100, 20, "§4Exit Menu"));
		buttonList.add(compactButton = new GuiButton(4, width - 80, 0, 80, 20, "§6Compact Menu"));
	}

	private void switchGui(int id) {
		mc.player.openGui(ItemEditorMod.instance, id, mc.world, (int) mc.player.posX, (int) mc.player.posY,
				(int) mc.player.posZ);
	}

}
