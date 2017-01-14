package com.franckyi.itemeditor.gui;

import java.io.IOException;

import com.franckyi.itemeditor.helper.ModHelper;
import com.franckyi.itemeditor.packet.EditNameMessage;
import com.franckyi.itemeditor.packet.ModPacketHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

public class GuiEditDisplay extends GuiUpdaterScreen {

	public GuiEditDisplay(int previousScreen) {
		super(previousScreen);
	}

	private GuiButton nameFormatButton, loreButton, hideFlagsButton;
	private GuiTextField itemName;

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.nameFormatButton) {
			itemName.setText(itemName.getText() + "§");
			itemName.setFocused(true);
		}
		if (button == this.loreButton) {
			ModHelper.currentItemName = itemName.getText();
			switchGui(ModGuiHandler.ITEM_EDITOR_LORE);
		}
		if (button == this.hideFlagsButton) {
			ModHelper.currentItemName = itemName.getText();
			switchGui(ModGuiHandler.ITEM_EDITOR_HIDEFLAGS);
		}
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawString(fontRendererObj, "Edit Item Display", this.width / 2 - 40, this.height / 2 - 90, 0x5555ff);
		drawString(fontRendererObj, "Name :", this.width / 2 - 100, this.height / 2 - 49, 0xffffff);
		itemName.drawTextBox();
	}

	@Override
	public void initGui() {
		buttonList.add(doneButton = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 35, 90, 20, "§2Done"));
		buttonList.add(cancelButton = new GuiButton(1, this.width / 2 + 10, this.height / 2 + 35, 90, 20, "§4Cancel"));
		itemName = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 50, this.height / 2 - 55, 120, 20);
		buttonList.add(nameFormatButton = new GuiButton(2, this.width / 2 + 80, this.height / 2 - 55, 20, 20, "§5§"));
		buttonList.add(loreButton = new GuiButton(3, this.width / 2 - 100, this.height / 2 - 30, "Edit Lore..."));
		buttonList.add(
				hideFlagsButton = new GuiButton(3, this.width / 2 - 100, this.height / 2 - 5, "Edit Hide Flags..."));
		itemName.setMaxStringLength(64);
		if (ModHelper.currentItemName != null)
			itemName.setText(ModHelper.currentItemName);
		else if (ModHelper.clientStack.getDisplayName().startsWith("§r"))
			itemName.setText(ModHelper.clientStack.getDisplayName().substring(2,
					ModHelper.clientStack.getDisplayName().length()));
		else
			itemName.setText(ModHelper.clientStack.getDisplayName());
		itemName.setFocused(true);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		itemName.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		itemName.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		itemName.updateCursorCounter();
		super.updateScreen();
	}

	@Override
	protected void updateServer() {
		ModPacketHandler.INSTANCE.sendToServer(new EditNameMessage(itemName.getText()));
	}

}
