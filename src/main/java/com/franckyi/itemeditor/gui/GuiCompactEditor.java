package com.franckyi.itemeditor.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.gui.child.GuiDoubleTextField;
import com.franckyi.itemeditor.gui.child.GuiEnchantList;
import com.franckyi.itemeditor.gui.child.GuiEnumButton;
import com.franckyi.itemeditor.helper.AttributeHelper;
import com.franckyi.itemeditor.helper.AttributeHelper.EnumAttributeOperation;
import com.franckyi.itemeditor.helper.AttributeHelper.EnumAttributeSlot;
import com.franckyi.itemeditor.helper.AttributeHelper.ItemAttribute;
import com.franckyi.itemeditor.helper.EnchantmentHelper.EnchantmentListEntry;
import com.franckyi.itemeditor.helper.EnchantmentHelper.ItemEnchantment;
import com.franckyi.itemeditor.helper.HideFlagHelper;
import com.franckyi.itemeditor.helper.HideFlagHelper.ItemHideFlag;
import com.franckyi.itemeditor.helper.ModHelper;
import com.franckyi.itemeditor.packet.EditAttributesMessage;
import com.franckyi.itemeditor.packet.EditEnchantMessage;
import com.franckyi.itemeditor.packet.EditHideFlagsMessage;
import com.franckyi.itemeditor.packet.EditLoreMessage;
import com.franckyi.itemeditor.packet.EditNameMessage;
import com.franckyi.itemeditor.packet.ModPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiCompactEditor extends GuiUpdaterScreen {

	public GuiCompactEditor(int previousScreen, int guiScale) {
		super(previousScreen);
		previousGuiScale = guiScale;
	}
	
	private List<ItemAttribute> attributesMessage = new ArrayList<ItemAttribute>();
	private List<ItemEnchantment> enchantmentMessage = new ArrayList<ItemEnchantment>();
	private List<ItemHideFlag> hideFlagsMessage = new ArrayList<ItemHideFlag>();
	private List<String> loresMessage = new ArrayList<String>();
	
	private GuiTextField itemName;
	private GuiTextField[] itemLores = new GuiTextField[ItemEditorMod.config.loreLineNumber];
	private GuiDoubleTextField[] itemAttributes = new GuiDoubleTextField[5];
	
	private GuiEnchantList enchList;
	private GuiCheckBox unbreakable;
	
	private GuiButton menuButton, reloadScreen;
	
	private GuiButton nameFormatButton;
	private GuiButton[] loreFormatButtons =  new GuiButton[ItemEditorMod.config.loreLineNumber];
	private GuiCheckBox[] hideFlags = new GuiCheckBox[6];
	private GuiEnumButton<Integer>[] operations = new GuiEnumButton[5];
	private GuiEnumButton<String>[] slots = new GuiEnumButton[5];
	
	private int previousGuiScale;

	@Override
	public void initGui() {
		buttonList.add(doneButton = new GuiButton(102, width/2 - 100, height - 40, 90, 20, "§2Save All"));
		buttonList.add(cancelButton = new GuiButton(103, width/2 + 10, height - 40, 90, 20, "§4Cancel"));
		buttonList.add(menuButton = new GuiButton(104, width-80, 0, 80, 20, "§6Normal Menu")); 
		buttonList.add(reloadScreen = new GuiButton(105, width-80, height-20, 80, 20, "§3Reload Screen"));
		
		itemName = new GuiTextField(1, fontRendererObj, width/10, height/8, 100, 20);
		buttonList.add(nameFormatButton = new GuiButton(101, width/10 + 110, height/8, 20, 20, "§5§"));
		itemName.setMaxStringLength(64);
		if (ModHelper.currentItemName != null)
			itemName.setText(ModHelper.currentItemName);
		else if (ModHelper.clientStack.getDisplayName().startsWith("§r"))
			itemName.setText(ModHelper.clientStack.getDisplayName().substring(2, ModHelper.clientStack.getDisplayName().length()));
		else
			itemName.setText(ModHelper.clientStack.getDisplayName());
		itemName.setFocused(true);
		
		for(int i = 0; i < itemLores.length; i++){
			itemLores[i] = new GuiTextField(10 + i, fontRendererObj, width/10, height/8 + 40 +(30*i), 100, 20);
			buttonList.add(loreFormatButtons[i] = new GuiButton(110 + i, width/10 + 110, height/8 + 40 +(30*i), 20, 20, "§5§"));
			String lore = ModHelper.clientStack.getOrCreateSubCompound("display").getTagList("Lore", 8)
					.getStringTagAt(i);
			if (lore.startsWith("§r")) {
				itemLores[i].setText(lore.substring(2, lore.length()));
			} else {
				itemLores[i].setText(lore);
			}
		}
		
		enchList = new GuiEnchantList(mc, 180, height - height/4, height/8, height - height/8, width/3 - 30, 25, width, height, this);
		buttonList.add(unbreakable = new GuiCheckBox(106, width/3 - 30, height - height/8 + 5, "Unbreakable", ModHelper.getOrCreateClientTagCompound().getInteger("Unbreakable") == 1));
		
		for(int i = 0; i < itemAttributes.length; i++){
			itemAttributes[i] = new GuiDoubleTextField(20 + i, fontRendererObj, 2*width/3 + 15, height/3 + (30*i), 40, 20);
			buttonList.add(operations[i] = new GuiEnumButton(30 + i, 2*width/3 + 70, height/3 + (30*i), 100, 20, "", EnumAttributeOperation.class));
			buttonList.add(slots[i] = new GuiEnumButton(30 + i, 2*width/3 + 180, height/3 + (30*i), 70, 20, "", EnumAttributeSlot.class));
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
		
		for(int i = 0; i < hideFlags.length; i++){
			buttonList.add(hideFlags[i] = new GuiCheckBox(40 + i, width/10 - 20, 7*height/10 + 20*i, " " + HideFlagHelper.getFlagFromID(i).getText(), false));
			hideFlags[i].setIsChecked(HideFlagHelper.hasFlag(HideFlagHelper.getFlagFromID(i), mc));
		}

	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button == menuButton)
			this.switchGui(ModGuiHandler.ITEM_EDITOR_MENU);
		if(button == reloadScreen){
			this.switchGui(ModGuiHandler.ITEM_EDITOR_MENU);
			this.switchGui(ModGuiHandler.ITEM_EDITOR_COMPACT);
		}
		if (button == this.nameFormatButton) {
			itemName.setText(itemName.getText() + "§");
			itemName.setFocused(true);
		}
		for (int i = 0; i < itemLores.length; i++) {
			if (button == loreFormatButtons[i]) {
				itemLores[i].setText(itemLores[i].getText() + "§");
				itemLores[i].setFocused(true);
			}
		}
		super.actionPerformed(button);
		if(button == doneButton || button == cancelButton){
			this.mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null)
				this.mc.setIngameFocus();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawString(fontRendererObj, "Item Editor GUI [Compact]", width/2 - width/16, 10, 0x5555ff);
		drawString(fontRendererObj, "Edit Display :", width/10 + 10, height/8 - 20, 0xffff00);
		drawString(fontRendererObj, "Name :", width/10 - 50, height/8 + 6, 0xffffff);
		drawString(fontRendererObj, "Edit Hide Flags :", width/10 - 10, 7*height/10 - 20, 0xffff00);
		itemName.drawTextBox();
		for(int i = 0; i < itemLores.length; i++){
			itemLores[i].drawTextBox();
			drawString(fontRendererObj, "Lore " + i + " :", width/10 - 50, height/8 + 46 +(30*i), 0xffffff);
		}
		drawString(fontRendererObj, "Edit Attributes :", 2*width/3 + 15, height/3 - 20, 0xffff00);
		for(int i = 0; i < itemAttributes.length; i++){
			itemAttributes[i].drawTextBox();
			drawString(fontRendererObj, AttributeHelper.getAttributeFromID(i).getText() + " :", 2*width/3 - 85, height/3 + (30*i) + 6, 0xffffff);
		}
		drawString(fontRendererObj, "Edit Enchantments :", width/3 + 10, height/8 - 20, 0xffff00);
		enchList.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		itemName.textboxKeyTyped(typedChar, keyCode);
		for(GuiTextField itemLore : itemLores)
			itemLore.textboxKeyTyped(typedChar, keyCode);
		for(EnchantmentListEntry ench : enchList.getEnchantmentList())
			ench.getTextField().textboxKeyTyped(typedChar, keyCode);
		for(GuiDoubleTextField itemAttribute : itemAttributes)
			itemAttribute.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		itemName.mouseClicked(mouseX, mouseY, mouseButton);
		for(GuiTextField itemLore : itemLores)
			itemLore.mouseClicked(mouseX, mouseY, mouseButton);
		for(EnchantmentListEntry ench : enchList.getEnchantmentList())
			ench.getTextField().mouseClicked(mouseX, mouseY, mouseButton);
		for(GuiDoubleTextField itemAttribute : itemAttributes)
			itemAttribute.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		itemName.updateCursorCounter();
		for(GuiTextField itemLore : itemLores)
			itemLore.updateCursorCounter();
		for(EnchantmentListEntry ench : enchList.getEnchantmentList())
			ench.getTextField().updateCursorCounter();
		for(GuiDoubleTextField itemAttribute : itemAttributes)
			itemAttribute.updateCursorCounter();
		super.updateScreen();
	}
	
	@Override
	public void onGuiClosed() {
		Minecraft.getMinecraft().gameSettings.guiScale = previousGuiScale;
		super.onGuiClosed();
	}
	
	@Override
	protected void updateServer() {
		ModPacketHandler.INSTANCE.sendToServer(new EditNameMessage(itemName.getText()));
		for (GuiTextField tf : itemLores)
			if (tf.getText() != "")
				loresMessage.add("§r" + tf.getText());
		ModPacketHandler.INSTANCE.sendToServer(new EditLoreMessage(loresMessage));
		for (int i = 0; i < enchList.getEnchantmentList().size(); i++) {
			if (enchList.getEnchantmentList().get(i).getTextField().getText().equals(""))
				enchList.getEnchantmentList().get(i).getTextField().setText("0");
			enchantmentMessage.add(new ItemEnchantment(enchList.getEnchantmentList().get(i).getEnch().getId(),
					enchList.getEnchantmentList().get(i).getTextField().getInt()));
		}
		if(unbreakable.isChecked())
			enchantmentMessage.add(new ItemEnchantment(420, 1));
		ModPacketHandler.INSTANCE.sendToServer(new EditEnchantMessage(enchantmentMessage));
		for(int i = 0; i < hideFlags.length; i++){
			hideFlagsMessage.add(new ItemHideFlag(HideFlagHelper.getFlagFromID(i), hideFlags[i].isChecked()));
		}
		ModPacketHandler.INSTANCE.sendToServer(new EditHideFlagsMessage(hideFlagsMessage));
		for(int i = 0; i < 5; i++)
			if(!itemAttributes[i].getText().equals("") && !itemAttributes[i].getText().equals("."))
				attributesMessage.add(new ItemAttribute(AttributeHelper.getAttributeFromID(i), itemAttributes[i].getDouble(), operations[i].getValue(), slots[i].getValue()));
		ModPacketHandler.INSTANCE.sendToServer(new EditAttributesMessage(attributesMessage));
	}

}
