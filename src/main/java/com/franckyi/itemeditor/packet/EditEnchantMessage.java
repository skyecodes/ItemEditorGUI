package com.franckyi.itemeditor.packet;

import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.helper.ModHelper;
import com.franckyi.itemeditor.helper.EnchantmentHelper.ItemEnchantment;

import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EditEnchantMessage implements IMessage {

	public EditEnchantMessage() {
	}

	private List<ItemEnchantment> enchants;

	public EditEnchantMessage(List<ItemEnchantment> message) {
		this.enchants = message;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		enchants = new ArrayList<ItemEnchantment>();
		String[] bufEnchants = ByteBufUtils.readUTF8String(buf).split(";");
		for (String enchant : bufEnchants)
			enchants.add(new ItemEnchantment(Integer.parseInt(enchant.split(":")[0]),
					Integer.parseInt(enchant.split(":")[1])));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		String bufEnchants = "";
		for (ItemEnchantment enchant : enchants)
			bufEnchants += enchant.getEnch() + ":" + enchant.getLevel() + ";";
		ByteBufUtils.writeUTF8String(buf, bufEnchants.substring(0, bufEnchants.length() - 1));
	}

	public static class EditEnchantMessageHandler implements IMessageHandler<EditEnchantMessage, IMessage> {

		@Override
		public IMessage onMessage(final EditEnchantMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					if (ModHelper.serverStack.getTagCompound() != null){
						ModHelper.serverStack.getTagCompound().removeTag("ench");
						ModHelper.serverStack.getTagCompound().removeTag("Unbreakable");
					}
					for (ItemEnchantment ench : message.enchants){
						if (ench.getEnch() == 420){
							ModHelper.getOrCreateServerTagCompound().setInteger("Unbreakable", 1);
						}
						else if (ench.getLevel() != 0)
							ModHelper.serverStack.addEnchantment(Enchantment.getEnchantmentByID(ench.getEnch()),
									ench.getLevel());
					}
				}
			});
			return null;
		}

	}

}
