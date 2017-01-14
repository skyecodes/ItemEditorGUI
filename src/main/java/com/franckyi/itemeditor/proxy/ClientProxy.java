package com.franckyi.itemeditor.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements CommonProxy {

	public static KeyBinding keyBinding;

	@Override
	public void registerKeyBindings() {
		keyBinding = new KeyBinding("key.itemeditor.open", Keyboard.KEY_I, "key.itemeditor.category");
		ClientRegistry.registerKeyBinding(keyBinding);
	}

}
