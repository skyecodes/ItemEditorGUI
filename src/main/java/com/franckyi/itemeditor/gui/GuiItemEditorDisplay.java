package com.franckyi.itemeditor.gui;

import java.io.IOException;
import java.util.Arrays;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.misc.HideFlagHelper;
import com.franckyi.itemeditor.misc.HideFlagHelper.EnumHideFlags;
import com.franckyi.itemeditor.misc.HideFlagHelper.HideFlag;
import com.franckyi.itemeditor.misc.SharedContent;
import com.franckyi.itemeditor.packet.EditItemNameMessage;
import com.franckyi.itemeditor.packet.ItemEditorPacketHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiItemEditorDisplay extends GuiScreen {

	private GuiButton cancelButton;
	private GuiButton doneButton;
	private GuiButton formatButton;
	private GuiButton loreButton;
	private GuiTextField name;
	private GuiCheckBox displayEnchants;

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.doneButton) {
			updateServer();
			updateClient();
		}
		if (button == this.cancelButton || button == this.doneButton)
			switchGui(ItemEditorGuiHandler.ITEM_EDITOR_MENU);
		if (button == this.formatButton) {
			name.setText(name.getText() + "§");
			name.setFocused(true);
		}
		if (button == this.loreButton) {
			SharedContent.currentItemName = name.getText();
			switchGui(ItemEditorGuiHandler.ITEM_EDITOR_LORE);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawString(fontRendererObj, "Edit Item Display", this.width / 2 - 40, this.height / 2 - 90, 0xffffff);
		drawString(fontRendererObj, "Name :", this.width / 2 - 100, this.height / 2 - 43, 0xffffff);
		name.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		ItemStack stack = mc.player.getHeldItemMainhand();
		buttonList.add(doneButton = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 30, 90, 20, "Done"));
		buttonList.add(cancelButton = new GuiButton(1, this.width / 2 + 10, this.height / 2 + 30, 90, 20, "Cancel"));
		name = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 50, this.height / 2 - 50, 120, 20);
		buttonList.add(formatButton = new GuiButton(2, this.width / 2 + 80, this.height / 2 - 50, 20, 20, "§"));
		buttonList.add(loreButton = new GuiButton(3, this.width / 2 - 100, this.height / 2 - 25, "Edit Lore..."));
		buttonList.add(displayEnchants = new GuiCheckBox(10, this.width / 2 - 100, this.height / 2, " Display Enchantments", true));
		name.setMaxStringLength(32);
		if (SharedContent.currentItemName != null) {
			name.setText(SharedContent.currentItemName);
		} else if (stack.getDisplayName().startsWith("§r")) {
			name.setText(stack.getDisplayName().substring(2, stack.getDisplayName().length()));
		} else {
			name.setText(stack.getDisplayName());
		}
		name.setFocused(true);
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		super.keyTyped(par1, par2);
		name.textboxKeyTyped(par1, par2);
	}

	protected void mouseClicked(int x, int y, int btn) throws IOException {
		super.mouseClicked(x, y, btn);
		name.mouseClicked(x, y, btn);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		name.updateCursorCounter();
	}

	private void updateServer() {
		ItemEditorPacketHandler.INSTANCE.sendToServer(new EditItemNameMessage(name.getText(), Arrays.asList(new HideFlag(EnumHideFlags.ENCHANTMENTS, displayEnchants.isChecked()))));
	}

	private void updateClient() {
		mc.player.getHeldItemMainhand().setStackDisplayName("§r" + name.getText());
		mc.player.getHeldItemMainhand().getTagCompound().setInteger("HideFlags",
				HideFlagHelper.value(Arrays.asList(new HideFlag(EnumHideFlags.ENCHANTMENTS, displayEnchants.isChecked()))));
	}

	private void switchGui(int id) {
		mc.player.openGui(ItemEditorMod.instance, id, mc.world, (int) mc.player.posX, (int) mc.player.posY,
				(int) mc.player.posZ);
	}

}
