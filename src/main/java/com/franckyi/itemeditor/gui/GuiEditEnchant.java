package com.franckyi.itemeditor.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.gui.child.GuiEnchantList;
import com.franckyi.itemeditor.helper.ModHelper;
import com.franckyi.itemeditor.helper.EnchantmentHelper.EnchantmentListEntry;
import com.franckyi.itemeditor.helper.EnchantmentHelper.ItemEnchantment;
import com.franckyi.itemeditor.packet.EditEnchantMessage;
import com.franckyi.itemeditor.packet.ModPacketHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiEditEnchant extends GuiUpdaterScreen {

	public GuiEditEnchant(int previousScreen) {
		super(previousScreen);
	}

	private List<ItemEnchantment> enchantmentMessage = new ArrayList<ItemEnchantment>();
	private GuiEnchantList enchList;
	private GuiCheckBox unbreakable;

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		enchList.actionPerformed(button);
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		enchList.drawScreen(mouseX, mouseY, partialTicks);
		drawString(fontRendererObj, "Edit Item Enchantments", this.width / 2 - 50,
				(int) (this.height / 2 - this.height / 2.5), 0x5555ff);
	}

	@Override
	public void initGui() {
		enchList = new GuiEnchantList(mc, width / 2, height, 50, height - 60, width / 4, 25, width, height, this);
		buttonList.add(unbreakable = new GuiCheckBox(2, this.width / 2 - 100, (int) (this.height / 2 + this.height / 2.6 - 20), "Unbreakable", ModHelper.getOrCreateClientTagCompound().getInteger("Unbreakable") == 1));
		buttonList.add(doneButton = new GuiButton(0, this.width / 2 - 100, (int) (this.height / 2 + this.height / 2.6),
				90, 20, "§2Done"));
		buttonList.add(cancelButton = new GuiButton(1, this.width / 2 + 10, (int) (this.height / 2 + this.height / 2.6),
				90, 20, "§4Cancel"));
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for(EnchantmentListEntry ench : enchList.getEnchantmentList())
			ench.getTextField().textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int x, int y, int btn) throws IOException {
		for(EnchantmentListEntry ench : enchList.getEnchantmentList())
			ench.getTextField().mouseClicked(x, y, btn);
		super.mouseClicked(x, y, btn);
	}

	@Override
	public void updateScreen() {
		for(EnchantmentListEntry ench : enchList.getEnchantmentList())
			ench.getTextField().updateCursorCounter();
		super.updateScreen();
	}

	@Override
	protected void updateServer() {
		for (int i = 0; i < enchList.getEnchantmentList().size(); i++) {
			if (enchList.getEnchantmentList().get(i).getTextField().getText().equals(""))
				enchList.getEnchantmentList().get(i).getTextField().setText("0");
			enchantmentMessage.add(new ItemEnchantment(enchList.getEnchantmentList().get(i).getEnch().getId(),
					enchList.getEnchantmentList().get(i).getTextField().getInt()));
		}
		if(unbreakable.isChecked())
			enchantmentMessage.add(new ItemEnchantment(420, 1));
		ModPacketHandler.INSTANCE.sendToServer(new EditEnchantMessage(enchantmentMessage));
	}

}
