package com.franckyi.itemeditor.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.helper.ModHelper;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public abstract class GuiUpdaterScreen extends GuiScreen {

	protected GuiButton cancelButton, doneButton;
	protected int previousScreen;

	public GuiUpdaterScreen(int previousScreen) {
		this.previousScreen = previousScreen;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == doneButton)
			updateServer();
		if (button == cancelButton || button == doneButton)
			switchGui(previousScreen);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public final boolean doesGuiPauseGame() {
		return ItemEditorMod.config.pauseGame;
	}

	public abstract void initGui();

	protected abstract void updateServer();

	protected void switchGui(int screen) {
		mc.player.openGui(ItemEditorMod.instance, screen, mc.world, (int) mc.player.posX, (int) mc.player.posY,
				(int) mc.player.posZ);
	}

}
