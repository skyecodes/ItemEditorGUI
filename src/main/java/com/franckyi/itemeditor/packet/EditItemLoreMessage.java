package com.franckyi.itemeditor.packet;

import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EditItemLoreMessage implements IMessage {

	public EditItemLoreMessage() {
	}

	private List<String> lores;

	public EditItemLoreMessage(List<String> message) {
		this.lores = message;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		String bufLore = ByteBufUtils.readUTF8String(buf);
		lores = Arrays.asList(bufLore.split("§§"));
		for (int i = 1; i < lores.size(); i++) {
			lores.set(i, "§" + lores.get(i));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		String bufLore = "";
		for (String lore : lores) {
			bufLore += lore + "§";
		}
		ByteBufUtils.writeUTF8String(buf, bufLore.substring(0, bufLore.length() - 1));
	}

	public static class EditItemLoreMessageHandler implements IMessageHandler<EditItemLoreMessage, IMessage> {

		@Override
		public IMessage onMessage(final EditItemLoreMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ctx.getServerHandler().playerEntity.getHeldItemMainhand().getOrCreateSubCompound("display").setTag("Lore",
							new NBTTagList());
					for (int i = 0; i < message.lores.size(); i++)
						ctx.getServerHandler().playerEntity.getHeldItemMainhand().getOrCreateSubCompound("display")
								.getTagList("Lore", 8).appendTag(new NBTTagString(message.lores.get(i)));

				}
			});
			return null;
		}

	}

}
