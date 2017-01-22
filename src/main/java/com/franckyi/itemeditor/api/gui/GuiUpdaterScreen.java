package com.franckyi.itemeditor.api.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiUpdaterScreen extends GuiScreen {

	protected GuiButton cancelButton, doneButton;
	
	protected int previousScreen;
	protected boolean doesGuiPauseGame;
	protected Object modInstance;
	
	
	public GuiUpdaterScreen(int previousScreen, boolean doesGuiPauseGame, Object modInstance) {
		this.previousScreen = previousScreen;
		this.doesGuiPauseGame = doesGuiPauseGame;
		this.modInstance = modInstance;
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
		return doesGuiPauseGame;
	}

	public abstract void initGui();

	protected abstract void updateServer();

	protected void switchGui(int screen) {
		mc.player.openGui(modInstance, screen, mc.world, (int) mc.player.posX, (int) mc.player.posY,
				(int) mc.player.posZ);
	}

}
