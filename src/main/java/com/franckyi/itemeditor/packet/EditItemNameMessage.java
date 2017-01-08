package com.franckyi.itemeditor.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EditItemNameMessage implements IMessage{
	
	public EditItemNameMessage(){}

	private String name;
	
	public EditItemNameMessage(String message) {
	    this.name = message;
	  }

	@Override
	public void fromBytes(ByteBuf buf) {
		name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, name);
	}
	
	public static class EditItemNameMessageHandler implements IMessageHandler<EditItemNameMessage, IMessage>{

		@Override
		public IMessage onMessage(final EditItemNameMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                	ctx.getServerHandler().playerEntity.getHeldItemMainhand().setStackDisplayName("§r" + message.name);
                }
            });
            return null;
		}
		
	}

}
