package com.franckyi.itemeditor.packet;

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

	private List<ItemHideFlag> hideFlags;

	public EditHideFlagsMessage(List<ItemHideFlag> hideFlags) {
		this.hideFlags = hideFlags;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		String bufFlags = ByteBufUtils.readUTF8String(buf);
		hideFlags = new ArrayList<ItemHideFlag>();
		for (String flag : bufFlags.split(";"))
			hideFlags.add(new ItemHideFlag(HideFlagHelper.getFlagFromValue(Integer.parseInt(flag.split(":")[0])),
					Boolean.parseBoolean(flag.split(":")[1])));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		String bufString = "";
		for (ItemHideFlag flag : hideFlags)
			bufString += flag.getFlagValue() + ":" + flag.isDisplayed() + ";";
		ByteBufUtils.writeUTF8String(buf, bufString.substring(0, bufString.length() - 1));
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
