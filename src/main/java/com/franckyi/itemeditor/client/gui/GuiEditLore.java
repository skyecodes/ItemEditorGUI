package com.franckyi.itemeditor.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.api.gui.GuiUpdaterScreen;
import com.franckyi.itemeditor.client.gui.child.GuiLoreList;
import com.franckyi.itemeditor.client.gui.child.GuiLoreList.LoreListEntry;
import com.franckyi.itemeditor.network.EditLoreMessage;
import com.franckyi.itemeditor.network.ModPacketHandler;

import net.minecraft.client.gui.GuiButton;

public class GuiEditLore extends GuiUpdaterScreen {

	public GuiEditLore(int previousScreen, boolean pauseGame, ItemEditorMod instance) {
		super(previousScreen, pauseGame, instance);
	}

	private GuiLoreList loreList;
	private List<String> loreMessage = new ArrayList<String>();

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawString(fontRendererObj, "Edit Item Lore", this.width / 2 - 35, this.height / 2 - 90, 0x5555ff);
		loreList.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		loreList = new GuiLoreList(mc, width / 2, height / 2, height / 4, 3 * height / 4, width / 4, 25, width, height,
				this);
		for (LoreListEntry entry : loreList.getLoreList())
			buttonList.add(entry.getFormatButton());
		buttonList.add(doneButton = new GuiButton(1, width / 2 - 100, 3 * height / 4 + 15, 90, 20, "§2Done"));
		buttonList.add(cancelButton = new GuiButton(2, width / 2 + 10, 3 * height / 4 + 15, 90, 20, "§4Cancel"));
		loreList.getLoreList().get(0).getTextField().setFocused(true);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (LoreListEntry entry : loreList.getLoreList())
			entry.getTextField().textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (LoreListEntry entry : loreList.getLoreList())
			entry.getTextField().mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		for (LoreListEntry entry : loreList.getLoreList())
			entry.getTextField().updateCursorCounter();
		super.updateScreen();
	}

	@Override
	protected void updateServer() {
		for (LoreListEntry entry : loreList.getLoreList())
			if (!entry.getTextField().getText().equals(""))
				loreMessage.add(entry.getTextField().getText());
		ModPacketHandler.INSTANCE.sendToServer(new EditLoreMessage(loreMessage));
	}

}
