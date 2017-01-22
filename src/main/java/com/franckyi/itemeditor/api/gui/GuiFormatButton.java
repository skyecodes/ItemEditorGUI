package com.franckyi.itemeditor.api.gui;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.ModReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiFormatButton extends GuiZButton {

	private GuiTextField textField;

	public GuiFormatButton(int buttonId, int x, int y, GuiTextField textField) {
		super(buttonId, x, y, 20, 20, "");
		this.textField = textField;
	}

	public GuiFormatButton(int buttonId, int x, int y, GuiTextField textField, float zLevel) {
		this(buttonId, x, y, textField);
		this.setZLevel(zLevel);
	}

	public GuiFormatButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height && this.enabled) {
			if (ItemEditorMod.config.formatCharacterAtTheEnd)
				textField.setText(textField.getText() + "§");
			else
				textField.setText(textField.getText().substring(0, textField.getCursorPosition()) + "§"
						+ textField.getText().substring(textField.getCursorPosition()));
			textField.setFocused(true);
		}
		return super.mousePressed(mc, mouseX, mouseY);
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		super.drawButton(mc, mouseX, mouseY);
		if (this.enabled) {
			mc.getTextureManager()
					.bindTexture(new ResourceLocation(ModReference.MODID, "textures/gui/formatbuttonicon.png"));
			this.drawModalRectWithCustomSizedTexture(this.xPosition + 2, this.yPosition + 2, 0, 0, 16, 16, 16, 16, 2);
		}
	}

	private void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height,
			float textureWidth, float textureHeight, double zLevel) {
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos((double) x, (double) (y + height), zLevel)
				.tex((double) (u * f), (double) ((v + (float) height) * f1)).endVertex();
		vertexbuffer.pos((double) (x + width), (double) (y + height), zLevel)
				.tex((double) ((u + (float) width) * f), (double) ((v + (float) height) * f1)).endVertex();
		vertexbuffer.pos((double) (x + width), (double) y, zLevel)
				.tex((double) ((u + (float) width) * f), (double) (v * f1)).endVertex();
		vertexbuffer.pos((double) x, (double) y, zLevel).tex((double) (u * f), (double) (v * f1)).endVertex();
		tessellator.draw();
	}

}
