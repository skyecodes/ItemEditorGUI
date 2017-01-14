package com.franckyi.itemeditor.packet;

import com.franckyi.itemeditor.helper.ModHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EditNameMessage implements IMessage {

	public EditNameMessage() {
	}

	private String name;

	public EditNameMessage(String name) {
		this.name = name;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, name);
	}

	public static class EditNameMessageHandler implements IMessageHandler<EditNameMessage, IMessage> {

		@Override
		public IMessage onMessage(final EditNameMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ModHelper.serverStack.setStackDisplayName("§r" + message.name);
				}
			});
			return null;
		}

	}

}
