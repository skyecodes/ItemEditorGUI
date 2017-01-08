package com.franckyi.itemeditor.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements CommonProxy{
	
	public static KeyBinding[] keyBindings;

	@Override
	public void registerKeyBindings() {
		keyBindings = new KeyBinding[1];
		keyBindings[0] = new KeyBinding("key.gui.desc", Keyboard.KEY_I, "key.itemeditor.category");
		for (KeyBinding keyBinding : keyBindings) {
		    ClientRegistry.registerKeyBinding(keyBinding);
		}
	}

}
