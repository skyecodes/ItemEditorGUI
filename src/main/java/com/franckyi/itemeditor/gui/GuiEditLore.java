package com.franckyi.itemeditor.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.helper.ModHelper;
import com.franckyi.itemeditor.packet.EditLoreMessage;
import com.franckyi.itemeditor.packet.ModPacketHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

public class GuiEditLore extends GuiUpdaterScreen {

	public GuiEditLore(int previousScreen) {
		super(previousScreen);
	}

	private List<String> loresMessage = new ArrayList<String>();
	private int loreNumber, firstHeight;
	private GuiButton[] loreFormatButtons;
	private GuiTextField[] itemLores;

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		for (int i = 0; i < loreNumber; i++) {
			if (button == loreFormatButtons[i]) {
				itemLores[i].setText(itemLores[i].getText() + "§");
				itemLores[i].setFocused(true);
			}
		}
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawString(fontRendererObj, "Edit Item Lore", this.width / 2 - 35, this.height / 2 - 90, 0x5555ff);
		for (int i = 0; i < loreNumber; i++)
			drawString(fontRendererObj, "Lore " + (i + 1) + " :", this.width / 2 - 100, firstHeight + 25 * i + 7,
					0xffffff);
		for (GuiTextField field : itemLores)
			field.drawTextBox();
	}

	@Override
	public void initGui() {
		loreNumber = ItemEditorMod.config.loreLineNumber;
		loreFormatButtons = new GuiButton[loreNumber];
		firstHeight = height / 2 - (25 * loreNumber + 20) / 2;
		itemLores = new GuiTextField[loreNumber];
		for (int i = 0; i < loreNumber; i++) {
			itemLores[i] = new GuiTextField(i, fontRendererObj, width / 2 - 50, firstHeight + 25 * i, 120, 20);
			itemLores[i].setMaxStringLength(255);
			String lore = ModHelper.clientStack.getOrCreateSubCompound("display").getTagList("Lore", 8)
					.getStringTagAt(i);
			if (lore.startsWith("§r")) {
				itemLores[i].setText(lore.substring(2, lore.length()));
			} else {
				itemLores[i].setText(lore);
			}
			buttonList
					.add(loreFormatButtons[i] = new GuiButton(i, width / 2 + 80, firstHeight + 25 * i, 20, 20, "§5§"));
		}
		buttonList.add(doneButton = new GuiButton(loreNumber, this.width / 2 - 100, firstHeight + 25 * loreNumber + 10,
				90, 20, "§2Done"));
		buttonList.add(cancelButton = new GuiButton(loreNumber + 1, this.width / 2 + 10,
				firstHeight + 25 * loreNumber + 10, 90, 20, "§4Cancel"));
		itemLores[0].setFocused(true);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (GuiTextField field : itemLores)
			field.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (GuiTextField field : itemLores)
			field.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		for (GuiTextField field : itemLores)
			field.updateCursorCounter();
		super.updateScreen();
	}

	@Override
	protected void updateServer() {
		for (GuiTextField tf : itemLores)
			if (tf.getText() != "")
				loresMessage.add("§r" + tf.getText());
		ModPacketHandler.INSTANCE.sendToServer(new EditLoreMessage(loresMessage));
	}

}
