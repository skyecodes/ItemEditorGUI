package com.franckyi.itemeditor.gui;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.ModReference;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiItemEditorConfig extends GuiConfig {

	public GuiItemEditorConfig(GuiScreen parentScreen) {
		super(parentScreen,
				new ConfigElement(ItemEditorMod.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
				ModReference.MODID, false, false, GuiConfig.getAbridgedConfigPath(ItemEditorMod.config.toString()));
	}

}
