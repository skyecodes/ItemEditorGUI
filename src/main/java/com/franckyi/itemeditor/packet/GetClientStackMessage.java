package com.franckyi.itemeditor.packet;

import com.franckyi.itemeditor.helper.ModHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GetClientStackMessage implements IMessage {

	public GetClientStackMessage() {
	}

	private int inventoryStack;

	public GetClientStackMessage(int inventoryStack) {
		this.inventoryStack = inventoryStack;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		inventoryStack = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(inventoryStack);
	}

	public static class GetClientStackMessageHandler implements IMessageHandler<GetClientStackMessage, IMessage> {

		@Override
		public IMessage onMessage(final GetClientStackMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ModHelper.serverStack = ctx.getServerHandler().playerEntity.inventory
							.getStackInSlot(message.inventoryStack);
				}
			});
			return null;
		}

	}

}
