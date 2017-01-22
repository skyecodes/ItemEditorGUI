package com.franckyi.itemeditor.network;

import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.helper.HideFlagHelper;
import com.franckyi.itemeditor.helper.HideFlagHelper.ItemHideFlag;
import com.franckyi.itemeditor.helper.ModHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EditHideFlagsMessage implements IMessage {

	public EditHideFlagsMessage() {
	}

	private List<ItemHideFlag> hideFlags = new ArrayList<ItemHideFlag>();

	public EditHideFlagsMessage(List<ItemHideFlag> hideFlags) {
		this.hideFlags = hideFlags;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		String flag = ByteBufUtils.readUTF8String(buf);
		String[] part;
		while (flag != null && !flag.equals("")) {
			part = flag.split(":");
			hideFlags.add(new ItemHideFlag(HideFlagHelper.getFlagFromValue(Integer.parseInt(part[0])),
					Boolean.parseBoolean(part[1])));
			flag = (buf.isReadable()) ? ByteBufUtils.readUTF8String(buf) : "";
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for (ItemHideFlag flag : hideFlags)
			ByteBufUtils.writeUTF8String(buf, flag.getFlagValue() + ":" + flag.isDisplayed());
	}

	public static class EditHideFlagsMessageHandler implements IMessageHandler<EditHideFlagsMessage, IMessage> {

		@Override
		public IMessage onMessage(final EditHideFlagsMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ModHelper.getOrCreateServerTagCompound().setInteger("HideFlags",
							HideFlagHelper.value(message.hideFlags));
				}
			});
			return null;
		}

	}

}
