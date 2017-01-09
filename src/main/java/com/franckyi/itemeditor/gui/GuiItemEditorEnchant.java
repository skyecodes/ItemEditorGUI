package com.franckyi.itemeditor.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.gui.components.GuiItemEditorEnchantList;
import com.franckyi.itemeditor.misc.ModEnchantmentHelper.EnchantmentListEntry;
import com.franckyi.itemeditor.misc.ModEnchantmentHelper.ModEnchantment;
import com.franckyi.itemeditor.packet.EditItemEnchantMessage;
import com.franckyi.itemeditor.packet.EditItemLoreMessage;
import com.franckyi.itemeditor.packet.ItemEditorPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class GuiItemEditorEnchant extends GuiScreen {

	private GuiItemEditorEnchantList enchList;
	private GuiTextField[] enchLevels;
	private GuiButton cancelButton;
	private GuiButton doneButton;
	private GuiButton scrollUp, scrollDown;

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		enchList.actionPerformed(button);
		if (button == doneButton) {
			List<ModEnchantment> msg = createMessage();
			updateServer(msg);
			updateClient(msg);
		}
		if (button == this.cancelButton || button == this.doneButton)
			switchGui(ItemEditorGuiHandler.ITEM_EDITOR_MENU);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		enchList.drawScreen(mouseX, mouseY, partialTicks);
		drawString(fontRendererObj, "Edit Item Enchantments", this.width / 2 - 50,
				(int) (this.height / 2 - this.height / 2.5), 0xffffff);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		enchList = new GuiItemEditorEnchantList(mc, width, height, 50, height - 50, 25);
		for (EnchantmentListEntry ench : enchList.getEnchantmentList())
			ench.getTextField()
					.setText(EnchantmentHelper.getEnchantmentLevel(
							Enchantment.getEnchantmentByID(ench.getEnch().getId()), mc.player.getHeldItemMainhand())
							+ "");
		buttonList.add(doneButton = new GuiButton(0, this.width / 2 - 100, (int) (this.height / 2 + this.height / 2.6),
				90, 20, "Done"));
		buttonList.add(cancelButton = new GuiButton(0, this.width / 2 + 10, (int) (this.height / 2 + this.height / 2.6),
				90, 20, "Cancel"));
		buttonList.add(scrollUp = new GuiButton(7, this.width / 2 + this.width / 3, 60, 50, 20, "Up"));
		buttonList.add(scrollUp = new GuiButton(8, this.width / 2 + this.width / 3, height - 80, 50, 20, "Down"));
		this.enchList.registerScrollButtons(7, 8);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (EnchantmentListEntry ench : enchList.getEnchantmentList())
			if (Character.isDigit(typedChar) || keyCode == 14 || keyCode == 211)
				ench.getTextField().textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (EnchantmentListEntry ench : enchList.getEnchantmentList())
			ench.getTextField().mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		for (EnchantmentListEntry ench : enchList.getEnchantmentList())
			ench.getTextField().updateCursorCounter();
		super.updateScreen();
	}

	private List<ModEnchantment> createMessage() {
		List<ModEnchantment> msgList = new ArrayList<ModEnchantment>();
		for (EnchantmentListEntry ench : enchList.getEnchantmentList()) {
			if (ench.getTextField().getText().equals(""))
				ench.getTextField().setText("0");
			msgList.add(new ModEnchantment(ench.getEnch().getId(), Integer.parseInt(ench.getTextField().getText())));
		}
		return msgList;
	}

	private void updateServer(List<ModEnchantment> msg) {
		ItemEditorPacketHandler.INSTANCE.sendToServer(new EditItemEnchantMessage(createMessage()));
	}

	private void updateClient(List<ModEnchantment> msg) {
		if (mc.player.getHeldItemMainhand().getTagCompound() != null)
			mc.player.getHeldItemMainhand().getTagCompound().removeTag("ench");
		for (ModEnchantment ench : msg)
			if (ench.getLevel() != 0)
				mc.player.getHeldItemMainhand().addEnchantment(Enchantment.getEnchantmentByID(ench.getEnch()),
						ench.getLevel());
	}

	private void switchGui(int id) {
		mc.player.openGui(ItemEditorMod.instance, id, mc.world, (int) mc.player.posX, (int) mc.player.posY,
				(int) mc.player.posZ);
	}

}
