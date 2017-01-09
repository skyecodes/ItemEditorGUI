package com.franckyi.itemeditor.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class EditItemNameMessage implements IMessage {

	public EditItemNameMessage() {
	}

	private String name;
	private List<HideFlag> hideFlags;

	public EditItemNameMessage(String name, List<HideFlag> hideFlags) {
		this.hideFlags = hideFlags;
		this.name = "§r" + name;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		String[] bufName = ByteBufUtils.readUTF8String(buf).split("§§");
		name = bufName[1].substring(1, bufName[1].length());
		hideFlags = new ArrayList<HideFlag>();
		for (String flag : bufName[0].split(";"))
			hideFlags.add(new HideFlag(EnumHideFlags.getFlagFromValue(Integer.parseInt(flag.split(":")[0])),
					Boolean.parseBoolean(flag.split(":")[1])));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		String bufString = "";
		for (HideFlag flag : hideFlags)
			bufString += flag.getFlagValue() + ":" + flag.isDisplayed() + ";";
		ByteBufUtils.writeUTF8String(buf, bufString.substring(0, bufString.length() - 1) + "§" + name);
	}

	public static class EditItemNameMessageHandler implements IMessageHandler<EditItemNameMessage, IMessage> {

		@Override
		public IMessage onMessage(final EditItemNameMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ctx.getServerHandler().playerEntity.getHeldItemMainhand().setStackDisplayName("§r" + message.name);
					ctx.getServerHandler().playerEntity.getHeldItemMainhand().getTagCompound().setInteger("HideFlags",
							HideFlagHelper.value(message.hideFlags));
				}
			});
			return null;
		}

	}

}
