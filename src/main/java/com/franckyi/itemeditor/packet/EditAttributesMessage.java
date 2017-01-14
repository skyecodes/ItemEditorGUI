package com.franckyi.itemeditor.packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.franckyi.itemeditor.helper.AttributeHelper;
import com.franckyi.itemeditor.helper.ModHelper;
import com.franckyi.itemeditor.helper.AttributeHelper.ItemAttribute;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EditAttributesMessage implements IMessage{
	
	public EditAttributesMessage() {}
	
	private List<ItemAttribute> attributes;
	
	public EditAttributesMessage(List<ItemAttribute> attributes){
		this.attributes = attributes;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		String[] bufAttr = ByteBufUtils.readUTF8String(buf).split(";");
		attributes = new ArrayList<ItemAttribute>();
		for(String attr : bufAttr){
			String[] split = attr.split(":");
			attributes.add(new ItemAttribute(AttributeHelper.getAttributeFromName(split[0]), Double.parseDouble(split[1]), Integer.parseInt(split[2]), split[3]));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		String bufAttr = "";
		for(ItemAttribute attr : attributes)
			bufAttr += attr.getAttribute().getName() + ":" + attr.getAmount() + ":" + attr.getOperation() + ":" + attr.getSlot() + ";";
		ByteBufUtils.writeUTF8String(buf, bufAttr.substring(0, bufAttr.length() - 1));
	}
	
	public static class EditAttributesMessageHandler implements IMessageHandler<EditAttributesMessage, IMessage> {

		@Override
		public IMessage onMessage(final EditAttributesMessage message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ModHelper.serverStack.getOrCreateSubCompound("test");
					ModHelper.serverStack.getTagCompound().removeTag("test");
					ModHelper.serverStack.getTagCompound().setTag("AttributeModifiers", new NBTTagList());
					for(ItemAttribute attr : message.attributes){
						NBTTagCompound c = new NBTTagCompound();
						c.setString("AttributeName", attr.getAttribute().getName());
						c.setString("Name", attr.getAttribute().getName());
						c.setDouble("Amount", attr.getAmount());
						c.setInteger("Operation", attr.getOperation());
						c.setLong("UUIDMost", UUID.randomUUID().getMostSignificantBits());
						c.setLong("UUIDLeast", UUID.randomUUID().getLeastSignificantBits());
						if(!attr.getSlot().equals("any"))
							c.setString("Slot", attr.getSlot());
						ModHelper.serverStack.getTagCompound().getTagList("AttributeModifiers", 10).appendTag(c);
					}
				}
			});
			return null;
		}

	}

}
