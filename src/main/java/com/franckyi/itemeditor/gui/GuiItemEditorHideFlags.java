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
	private GuiCheckBox hideEnchants, hideAttributeModifiers, hideUnbreakable, hideCanDestroy, hideCanBePlacedOn, hidePotionEffects;
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
		buttonList.add(doneButton = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 15, 90, 40, "Done"));
		buttonList.add(cancelButton = new GuiButton(1, this.width / 2 + 10, this.height / 2 + 15, 90, 40, "Cancel"));
		buttonList.add(hideEnchants = new GuiCheckBox(10, width / 2 - width / 5, height / 2 - 60, " Hide Enchantments", false));
		buttonList.add(hideAttributeModifiers = new GuiCheckBox(11, width / 2 - width / 5, height / 2 - 45, " Hide Attribute Modifiers", false));
		buttonList.add(hideUnbreakable = new GuiCheckBox(12, width / 2 - width / 5, height / 2 - 30, " Hide 'Unbreakable'", false));
		buttonList.add(hideCanDestroy = new GuiCheckBox(13, width / 2 - width / 5, height / 2 - 15, " Hide 'Can Destroy'", false));
		buttonList.add(hideCanBePlacedOn = new GuiCheckBox(14, width / 2 - width / 5, height / 2 - 0, " Hide 'Can Be Placed On'", false));
		buttonList.add(hidePotionEffects = new GuiCheckBox(15, width / 2 - width / 5, height / 2 + 15, " Hide Potion Effects", false));
		hideEnchants.setIsChecked(HideFlagHelper.hasFlag(EnumHideFlags.ENCHANTMENTS, mc));
		hideAttributeModifiers.setIsChecked(HideFlagHelper.hasFlag(EnumHideFlags.MODIFIERS, mc));
		hideUnbreakable.setIsChecked(HideFlagHelper.hasFlag(EnumHideFlags.UNBREAKABLE, mc));
		hideCanDestroy.setIsChecked(HideFlagHelper.hasFlag(EnumHideFlags.CAN_DESTROY, mc));
		hideCanBePlacedOn.setIsChecked(HideFlagHelper.hasFlag(EnumHideFlags.CAN_BE_PLACED_ON, mc));
		hidePotionEffects.setIsChecked(HideFlagHelper.hasFlag(EnumHideFlags.CAN_BE_PLACED_ON, mc));
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	private void updateServer() {
		hideFlags = Arrays.asList(new HideFlag(EnumHideFlags.ENCHANTMENTS, hideEnchants.isChecked()),
				new HideFlag(EnumHideFlags.MODIFIERS, hideAttributeModifiers.isChecked()),
				new HideFlag(EnumHideFlags.UNBREAKABLE, hideUnbreakable.isChecked()),
				new HideFlag(EnumHideFlags.CAN_DESTROY, hideCanDestroy.isChecked()),
				new HideFlag(EnumHideFlags.CAN_BE_PLACED_ON, hideCanBePlacedOn.isChecked()),
				new HideFlag(EnumHideFlags.POTION_EFFECTS, hidePotionEffects.isChecked()));
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
