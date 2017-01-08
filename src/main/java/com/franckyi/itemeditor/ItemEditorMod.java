package com.franckyi.itemeditor;

import com.franckyi.itemeditor.config.ItemEditorConfiguration;
import com.franckyi.itemeditor.event.EventHandlerCommon;
import com.franckyi.itemeditor.gui.ItemEditorGuiHandler;
import com.franckyi.itemeditor.packet.EditItemEnchantMessage;
import com.franckyi.itemeditor.packet.EditItemEnchantMessage.EditItemEnchantMessageHandler;
import com.franckyi.itemeditor.packet.EditItemLoreMessage;
import com.franckyi.itemeditor.packet.EditItemLoreMessage.EditItemLoreMessageHandler;
import com.franckyi.itemeditor.packet.EditItemNameMessage;
import com.franckyi.itemeditor.packet.EditItemNameMessage.EditItemNameMessageHandler;
import com.franckyi.itemeditor.packet.ItemEditorPacketHandler;
import com.franckyi.itemeditor.proxy.CommonProxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = ModReference.MODID, name = ModReference.NAME, version = ModReference.VERSION, acceptedMinecraftVersions = ModReference.MCVERSION)
public class ItemEditorMod {

	@Instance
	public static ItemEditorMod instance;

	public static ItemEditorConfiguration config;

	@SidedProxy(clientSide = ModReference.CLIENT_PROXY_CLASS, serverSide = ModReference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	@EventHandler
	public static void preInit(FMLPreInitializationEvent e) {
		config = new ItemEditorConfiguration(e.getSuggestedConfigurationFile());
	}

	@EventHandler
	public static void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ItemEditorGuiHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandlerCommon());
		proxy.registerKeyBindings();
		ItemEditorPacketHandler.INSTANCE.registerMessage(EditItemNameMessageHandler.class, EditItemNameMessage.class, 0,
				Side.SERVER);
		ItemEditorPacketHandler.INSTANCE.registerMessage(EditItemLoreMessageHandler.class, EditItemLoreMessage.class, 1,
				Side.SERVER);
		ItemEditorPacketHandler.INSTANCE.registerMessage(EditItemEnchantMessageHandler.class,
				EditItemEnchantMessage.class, 2, Side.SERVER);
	}

}
