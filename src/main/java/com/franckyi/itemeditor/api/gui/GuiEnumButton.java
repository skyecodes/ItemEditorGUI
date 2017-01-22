package com.franckyi.itemeditor.api.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiEnumButton<V> extends GuiZButton {

	private List<IEnumButtonField> fields = new ArrayList<IEnumButtonField>();
	private int i = 0;

	public GuiEnumButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText,
			Class<IEnumButtonField<V>> class1) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		for (IEnumButtonField enumVal : class1.getEnumConstants()) {
			fields.add(enumVal);
		}
		displayString = fields.get(0).getButtonText();
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height && this.enabled) {
			i = (i != fields.size() - 1) ? i + 1 : 0;
			displayString = fields.get(i).getButtonText();
		}
		return super.mousePressed(mc, mouseX, mouseY);
	}

	public V getValue() {
		return (V) fields.get(i).getButtonValue();
	}

	public void setValue(V value) {
		for (int i = 0; i < fields.size(); i++)
			if (fields.get(i).getButtonValue().equals(value)) {
				this.i = i;
				displayString = fields.get(i).getButtonText();
			}

	}

}
