package com.franckyi.itemeditor.packet;

import java.util.Arrays;
import java.util.List;

import com.franckyi.itemeditor.helper.ModHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagEnd;
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

	private List<String> lores;

	public EditLoreMessage(List<String> message) {
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
		if(bufLore.equals(""))
			ByteBufUtils.writeUTF8String(buf, "§r");
		else
			ByteBufUtils.writeUTF8String(buf, bufLore.substring(0, bufLore.length() - 1));
	}

	public static class EditLoreMessageHandler implements IMessageHandler<EditLoreMessage, IMessage> {

		@Override
		public IMessage onMessage(final EditLoreMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ModHelper.serverStack.getOrCreateSubCompound("display").setTag("Lore", new NBTTagList());
					for (int i = 0; i < message.lores.size(); i++)
						if (!message.lores.get(i).equals("§r"))
							ModHelper.serverStack.getOrCreateSubCompound("display").getTagList("Lore", 8)
									.appendTag(new NBTTagString(message.lores.get(i)));
				}
			});
			return null;
		}

	}

}
