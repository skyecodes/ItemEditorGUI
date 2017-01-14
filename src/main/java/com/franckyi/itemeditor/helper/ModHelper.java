package com.franckyi.itemeditor.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModHelper {

	public static ItemStack clientStack;
	public static ItemStack serverStack;
	public static String currentItemName;

	public static void clear() {
//		currentItemName = null;
//		clientStack = null;
//		serverStack = null;
	}
	
	public static NBTTagCompound getOrCreateClientTagCompound(){
		clientStack.getOrCreateSubCompound("test");
		clientStack.getTagCompound().removeTag("test");
		return clientStack.getTagCompound();
	}
	
	public static NBTTagCompound getOrCreateServerTagCompound(){
		serverStack.getOrCreateSubCompound("test");
		serverStack.getTagCompound().removeTag("test");
		return serverStack.getTagCompound();
	}

}
