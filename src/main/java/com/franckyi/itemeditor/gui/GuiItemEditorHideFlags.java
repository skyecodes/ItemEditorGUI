package com.franckyi.itemeditor.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.misc.HideFlagHelper;
import com.franckyi.itemeditor.misc.HideFlagHelper.EnumHideFlags;
import com.franckyi.itemeditor.misc.HideFlagHelper.HideFlag;
import com.franckyi.itemeditor.misc.SharedContent;
import com.franckyi.itemeditor.packet.EditItemHideFlagsMessage;
import com.franckyi.itemeditor.packet.ItemEditorPacketHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiItemEditorHideFlags extends GuiScreen {

	private List<HideFlag> hideFlags = new ArrayList<HideFlag>();
	private GuiCheckBox hideEnchants;
	private GuiButton doneButton, cancelButton;

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == doneButton) {
			updateServer();
			updateClient();
		}
		if (button == doneButton || button == cancelButton) {
			switchGui(ItemEditorGuiHandler.ITEM_EDITOR_DISPLAY);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawString(fontRendererObj, "Edit Item Hide Flags", this.width / 2 - 45, this.height / 2 - 90, 0xffffff);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		buttonList.add(
				hideEnchants = new GuiCheckBox(0, width / 2 - width / 5, height / 2 - 10, " Hide Enchantments", false));
		buttonList.add(doneButton = new GuiButton(10, this.width / 2 - 100, this.height / 2 + 15, 90, 20, "Done"));
		buttonList.add(cancelButton = new GuiButton(11, this.width / 2 + 10, this.height / 2 + 15, 90, 20, "Cancel"));
		hideEnchants.setIsChecked(HideFlagHelper.hasFlag(EnumHideFlags.ENCHANTMENTS, mc));
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	private void updateServer() {
		hideFlags = Arrays.asList(new HideFlag(EnumHideFlags.ENCHANTMENTS, hideEnchants.isChecked()));
		ItemEditorPacketHandler.INSTANCE.sendToServer(new EditItemHideFlagsMessage(hideFlags));

	}

	private void updateClient() {
		if(mc.player.getHeldItemMainhand().getTagCompound() == null){
			mc.player.getHeldItemMainhand().getOrCreateSubCompound("test");
			mc.player.getHeldItemMainhand().getTagCompound().removeTag("test");
		}
		mc.player.getHeldItemMainhand().getTagCompound().setInteger("HideFlags", HideFlagHelper.value(hideFlags));
	}

	private void switchGui(int id) {
		mc.player.openGui(ItemEditorMod.instance, id, mc.world, (int) mc.player.posX, (int) mc.player.posY,
				(int) mc.player.posZ);
	}

}
