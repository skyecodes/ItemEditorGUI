package com.franckyi.itemeditor.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.packet.EditItemLoreMessage;
import com.franckyi.itemeditor.packet.ItemEditorPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class GuiItemEditorLore extends GuiScreen {

	private List<String> loresString = new ArrayList<String>();
	private int loreNumber, firstHeight;
	private GuiTextField[] lores;
	private GuiButton[] formatButtons;
	private GuiButton cancelButton;
	private GuiButton doneButton;

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.doneButton) {
			for (GuiTextField tf : lores)
				if (tf.getText() != "")
					loresString.add("§r" + tf.getText());
			updateServer();
			updateClient();
		}
		if (button == this.cancelButton || button == this.doneButton)
			switchGui(ItemEditorGuiHandler.ITEM_EDITOR_DISPLAY);
		for (int i = 0; i < loreNumber; i++) {
			if (button == formatButtons[i]) {
				lores[i].setText(lores[i].getText() + "§");
				lores[i].setFocused(true);
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawString(fontRendererObj, "Edit Item Lore", this.width / 2 - 35, this.height / 2 - 90, 0xffffff);
		for (int i = 0; i < loreNumber; i++) {
			drawString(fontRendererObj, "Lore " + (i + 1), this.width / 2 - 100, firstHeight + 25 * i + 7, 0xffffff);
			lores[i].drawTextBox();
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		loreNumber = ItemEditorMod.config.loreLineNumber;
		lores = new GuiTextField[loreNumber];
		formatButtons = new GuiButton[loreNumber];
		firstHeight = height / 2 - (25 * loreNumber + 20) / 2;
		for (int i = 0; i < loreNumber; i++) {
			lores[i] = new GuiTextField(i, fontRendererObj, width / 2 - 50, firstHeight + 25 * i, 120, 20);
			lores[i].setMaxStringLength(255);
			String lore = mc.player.getHeldItemMainhand().getOrCreateSubCompound("display").getTagList("Lore", 8)
					.getStringTagAt(i);
			if (lore.startsWith("§r")) {
				lores[i].setText(lore.substring(2, lore.length()));
			} else {
				lores[i].setText(lore);
			}
			buttonList.add(formatButtons[i] = new GuiButton(i, width / 2 + 80, firstHeight + 25 * i, 20, 20, "§"));
		}
		buttonList.add(doneButton = new GuiButton(loreNumber, this.width / 2 - 100, firstHeight + 25 * loreNumber + 10,
				90, 20, "Done"));
		buttonList.add(cancelButton = new GuiButton(loreNumber + 1, this.width / 2 + 10,
				firstHeight + 25 * loreNumber + 10, 90, 20, "Cancel"));
		lores[0].setFocused(true);
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		super.keyTyped(par1, par2);
		for (GuiTextField lore : lores)
			lore.textboxKeyTyped(par1, par2);
	}

	protected void mouseClicked(int x, int y, int btn) throws IOException {
		super.mouseClicked(x, y, btn);
		for (GuiTextField lore : lores)
			lore.mouseClicked(x, y, btn);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		for (GuiTextField lore : lores)
			lore.updateCursorCounter();
	}

	private void updateServer() {
		ItemEditorPacketHandler.INSTANCE.sendToServer(new EditItemLoreMessage(loresString));
	}

	private void updateClient() {
		mc.player.getHeldItemMainhand().getOrCreateSubCompound("display").setTag("Lore", new NBTTagList());
		for (int i = 0; i < loresString.size(); i++) {
			mc.player.getHeldItemMainhand().getOrCreateSubCompound("display").getTagList("Lore", 8)
					.appendTag(new NBTTagString(loresString.get(i)));
		}
	}

	private void switchGui(int id) {
		mc.player.openGui(ItemEditorMod.instance, id, mc.world, (int) mc.player.posX, (int) mc.player.posY,
				(int) mc.player.posZ);
	}

}
