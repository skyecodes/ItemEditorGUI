package com.franckyi.itemeditor.gui;

import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.helper.HideFlagHelper;
import com.franckyi.itemeditor.helper.HideFlagHelper.ItemHideFlag;
import com.franckyi.itemeditor.packet.EditHideFlagsMessage;
import com.franckyi.itemeditor.packet.ModPacketHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiEditHideFlags extends GuiUpdaterScreen {

	public GuiEditHideFlags(int previousScreen) {
		super(previousScreen);
	}

	private List<ItemHideFlag> hideFlagsMessage = new ArrayList<ItemHideFlag>();
	private GuiCheckBox[] hideFlags = new GuiCheckBox[6];

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawString(fontRendererObj, "Edit Item Hide Flags", this.width / 2 - 45, this.height / 2 - 90, 0x5555ff);
	}

	@Override
	public void initGui() {
		buttonList.add(doneButton = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 35, 90, 20, "§2Done"));
		buttonList.add(cancelButton = new GuiButton(1, this.width / 2 + 10, this.height / 2 + 35, 90, 20, "§4Cancel"));
		for (int i = 0; i < hideFlags.length; i++) {
			buttonList.add(hideFlags[i] = new GuiCheckBox(i + 10, width / 2 - width / 5, height / 2 - 65 + (15 * i),
					" " + HideFlagHelper.getFlagFromID(i).getText(), false));
			hideFlags[i].setIsChecked(HideFlagHelper.hasFlag(HideFlagHelper.getFlagFromID(i), mc));
		}
	}

	@Override
	protected void updateServer() {
		for (int i = 0; i < hideFlags.length; i++) {
			hideFlagsMessage.add(new ItemHideFlag(HideFlagHelper.getFlagFromID(i), hideFlags[i].isChecked()));
		}
		ModPacketHandler.INSTANCE.sendToServer(new EditHideFlagsMessage(hideFlagsMessage));

	}

}
