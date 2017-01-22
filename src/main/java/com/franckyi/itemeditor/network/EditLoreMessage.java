package com.franckyi.itemeditor.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.franckyi.itemeditor.ItemEditorMod;
import com.franckyi.itemeditor.helper.ModHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EditLoreMessage implements IMessage {

	public EditLoreMessage() {
	}

	private List<String> lores = new ArrayList<String>();

	public EditLoreMessage(List<String> message) {
		this.lores = message;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		String bufLore = ByteBufUtils.readUTF8String(buf);
		while(bufLore != null && !bufLore.equals("")){
			lores.add(bufLore);
			bufLore = (buf.isReadable()) ? ByteBufUtils.readUTF8String(buf) : "";
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for (String lore : lores)
			ByteBufUtils.writeUTF8String(buf, lore);
	}

	public static class EditLoreMessageHandler implements IMessageHandler<EditLoreMessage, IMessage> {

		@Override
		public IMessage onMessage(final EditLoreMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ModHelper.serverStack.getOrCreateSubCompound("display").setTag("Lore", new NBTTagList());
					for (int i = 0; i < ItemEditorMod.config.loreLineNumber; i++)
						if (!message.lores.get(i).equals("§r"))
							ModHelper.serverStack.getOrCreateSubCompound("display").getTagList("Lore", 8)
									.appendTag(new NBTTagString("§r" + message.lores.get(i)));
				}
			});
			return null;
		}

	}

}
