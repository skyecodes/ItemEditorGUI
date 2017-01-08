package com.franckyi.itemeditor.packet;

import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.misc.ModEnchantmentHelper.EnchantmentListEntry;
import com.franckyi.itemeditor.misc.ModEnchantmentHelper.ModEnchantment;

import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EditItemEnchantMessage implements IMessage {

	public EditItemEnchantMessage() {
	}

	private List<ModEnchantment> enchants;

	public EditItemEnchantMessage(List<ModEnchantment> message) {
		this.enchants = message;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		enchants = new ArrayList<ModEnchantment>();
		String[] bufEnchants = ByteBufUtils.readUTF8String(buf).split(";");
		for (String enchant : bufEnchants)
			enchants.add(new ModEnchantment(Integer.parseInt(enchant.split(":")[0]),
					Integer.parseInt(enchant.split(":")[1])));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		String bufEnchants = "";
		for (ModEnchantment enchant : enchants)
			bufEnchants += enchant.getEnch() + ":" + enchant.getLevel() + ";";
		ByteBufUtils.writeUTF8String(buf, bufEnchants.substring(0, bufEnchants.length() - 1));
	}

	public static class EditItemEnchantMessageHandler implements IMessageHandler<EditItemEnchantMessage, IMessage> {

		@Override
		public IMessage onMessage(final EditItemEnchantMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					if(ctx.getServerHandler().playerEntity.getHeldItemMainhand().getTagCompound() != null)
						ctx.getServerHandler().playerEntity.getHeldItemMainhand().getTagCompound().removeTag("ench");
					for (ModEnchantment ench : message.enchants)
						if (ench.getLevel() != 0)
							ctx.getServerHandler().playerEntity.getHeldItemMainhand().addEnchantment(
									Enchantment.getEnchantmentByID(ench.getEnch()), ench.getLevel());
				}
			});
			return null;
		}

	}

}
