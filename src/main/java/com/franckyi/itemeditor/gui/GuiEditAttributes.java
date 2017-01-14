package com.franckyi.itemeditor.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.franckyi.itemeditor.gui.child.GuiDoubleTextField;
import com.franckyi.itemeditor.gui.child.GuiEnumButton;
import com.franckyi.itemeditor.helper.AttributeHelper;
import com.franckyi.itemeditor.helper.AttributeHelper.EnumAttributeOperation;
import com.franckyi.itemeditor.helper.AttributeHelper.EnumAttributeSlot;
import com.franckyi.itemeditor.helper.AttributeHelper.ItemAttribute;
import com.franckyi.itemeditor.helper.ModHelper;
import com.franckyi.itemeditor.packet.EditAttributesMessage;
import com.franckyi.itemeditor.packet.ModPacketHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class GuiEditAttributes extends GuiUpdaterScreen {

	public GuiEditAttributes(int previousScreen) {
		super(previousScreen);
	}

	private List<ItemAttribute> attributesMessage = new ArrayList<ItemAttribute>();
	private GuiDoubleTextField[] itemAttributes = new GuiDoubleTextField[5];
	private GuiEnumButton<Integer>[] operations = new GuiEnumButton[5];
	private GuiEnumButton<String>[] slots = new GuiEnumButton[5];

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawString(fontRendererObj, "Edit Item Attributes", this.width / 2 - 40, this.height / 2 - 90, 0x5555ff);
		for(int i = 0; i < itemAttributes.length; i++){
			itemAttributes[i].drawTextBox();
			drawString(fontRendererObj, AttributeHelper.getAttributeFromID(i).getText() + " :", this.width / 2 - this.width / 3 - 20, this.height / 2 - 60 + i * 25, 0xffffff);
		}
	}

	@Override
	public void initGui() {
		buttonList.add(doneButton = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 70, 90, 20, "§2Done"));
		buttonList.add(cancelButton = new GuiButton(1, this.width / 2 + 10, this.height / 2 + 70, 90, 20, "§4Cancel"));
		for (int i = 0; i < 5; i++){
			itemAttributes[i] = new GuiDoubleTextField(0, this.fontRendererObj, this.width / 2 - 50,
					this.height / 2 - 66 + (i * 25), 40, 20);
			buttonList.add(operations[i] = new GuiEnumButton(i+10, this.width / 2, this.height / 2 - 66 + (i * 25), 100, 20, "", EnumAttributeOperation.class));
			buttonList.add(slots[i] = new GuiEnumButton(i+10, this.width / 2 + 110, this.height / 2 - 66 + (i * 25), 70, 20, "", EnumAttributeSlot.class));
			ModHelper.getOrCreateClientTagCompound();
			for(int j = 0; j < ModHelper.clientStack.getTagCompound().getTagList("AttributeModifiers", 10).tagCount(); j++){
				if(ModHelper.clientStack.getTagCompound().getTagList("AttributeModifiers", 10).getCompoundTagAt(j).getString("Name").equals(AttributeHelper.getAttributeFromID(i).getName())){
					itemAttributes[i].setText(ModHelper.clientStack.getTagCompound().getTagList("AttributeModifiers", 10)
							.getCompoundTagAt(j).getDouble("Amount") + "");
					operations[i].setValue(ModHelper.clientStack.getTagCompound().getTagList("AttributeModifiers", 10)
							.getCompoundTagAt(j).getInteger("Operation"));
					slots[i].setValue(ModHelper.clientStack.getTagCompound().getTagList("AttributeModifiers", 10)
							.getCompoundTagAt(j).getString("Slot"));
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for(GuiTextField field : itemAttributes)
			field.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for(GuiTextField field : itemAttributes)
			field.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		for(GuiTextField field : itemAttributes)
			field.updateCursorCounter();
		super.updateScreen();
	}

	@Override
	protected void updateServer() {
		for(int i = 0; i < 5; i++)
			if(!itemAttributes[i].getText().equals("") && !itemAttributes[i].getText().equals("."))
				attributesMessage.add(new ItemAttribute(AttributeHelper.getAttributeFromID(i), itemAttributes[i].getDouble(), operations[i].getValue(), slots[i].getValue()));
		ModPacketHandler.INSTANCE.sendToServer(new EditAttributesMessage(attributesMessage));
	}

}
