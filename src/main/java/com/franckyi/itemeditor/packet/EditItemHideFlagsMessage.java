package com.franckyi.itemeditor.packet;

import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.misc.HideFlagHelper;
import com.franckyi.itemeditor.misc.HideFlagHelper.EnumHideFlags;
import com.franckyi.itemeditor.misc.HideFlagHelper.HideFlag;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EditItemHideFlagsMessage implements IMessage{
	
	public EditItemHideFlagsMessage(){}
	
	private List<HideFlag> hideFlags;
	
	public EditItemHideFlagsMessage(List<HideFlag> hideFlags){
		this.hideFlags = hideFlags;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		String bufFlags = ByteBufUtils.readUTF8String(buf);
		hideFlags = new ArrayList<HideFlag>();
		for (String flag : bufFlags.split(";"))
			hideFlags.add(new HideFlag(EnumHideFlags.getFlagFromValue(Integer.parseInt(flag.split(":")[0])),
					Boolean.parseBoolean(flag.split(":")[1])));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		String bufString = "";
		for (HideFlag flag : hideFlags)
			bufString += flag.getFlagValue() + ":" + flag.isDisplayed() + ";";
		ByteBufUtils.writeUTF8String(buf, bufString.substring(0, bufString.length() - 1));
	}
	
	public static class EditItemHideFlagsMessageHandler implements IMessageHandler<EditItemHideFlagsMessage, IMessage> {

		@Override
		public IMessage onMessage(final EditItemHideFlagsMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ctx.getServerHandler().playerEntity.getHeldItemMainhand().getTagCompound().setInteger("HideFlags",
							HideFlagHelper.value(message.hideFlags));
				}
			});
			return null;
		}

	}

}
