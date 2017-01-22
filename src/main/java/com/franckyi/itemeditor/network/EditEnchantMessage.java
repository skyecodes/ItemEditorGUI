package com.franckyi.itemeditor.network;

import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.helper.EnchantmentHelper.ItemEnchantment;
import com.franckyi.itemeditor.helper.ModHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EditEnchantMessage implements IMessage {

	public EditEnchantMessage() {
	}

	private List<ItemEnchantment> enchants = new ArrayList<ItemEnchantment>();

	public EditEnchantMessage(List<ItemEnchantment> message) {
		this.enchants = message;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		String ench = ByteBufUtils.readUTF8String(buf);
		String[] part;
		while (ench != null && !ench.equals("")){
			part = ench.split(":");
			enchants.add(new ItemEnchantment(Integer.parseInt(part[0]), Integer.parseInt(part[1])));
			ench = (buf.isReadable()) ? ByteBufUtils.readUTF8String(buf) : "";
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for (ItemEnchantment enchant : enchants)
			ByteBufUtils.writeUTF8String(buf, enchant.getEnch() + ":" + enchant.getLevel());
	}

	public static class EditEnchantMessageHandler implements IMessageHandler<EditEnchantMessage, IMessage> {

		@Override
		public IMessage onMessage(final EditEnchantMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					if (ModHelper.serverStack.getTagCompound() != null) {
						ModHelper.serverStack.getTagCompound().removeTag("ench");
						ModHelper.serverStack.getTagCompound().removeTag("Unbreakable");
					}
					for (ItemEnchantment ench : message.enchants) {
						if (ench.getEnch() == 420) {
							ModHelper.getOrCreateServerTagCompound().setInteger("Unbreakable", 1);
						} else if (ench.getLevel() != 0)
							ModHelper.serverStack.addEnchantment(Enchantment.getEnchantmentByID(ench.getEnch()),
									ench.getLevel());
					}
				}
			});
			return null;
		}

	}

}
