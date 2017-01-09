package com.franckyi.itemeditor.misc;

import java.util.List;

import com.franckyi.itemeditor.misc.HideFlagHelper.EnumHideFlags;
import com.franckyi.itemeditor.misc.HideFlagHelper.HideFlag;

import net.minecraft.client.Minecraft;

public class HideFlagHelper {
	
	public static class HideFlag{
		
		private EnumHideFlags flag;
		private boolean displayed;
		
		public HideFlag(EnumHideFlags flag, boolean displayed){
			this.flag = flag;
			this.displayed = displayed;
		}

		public EnumHideFlags getFlag() {
			return flag;
		}

		public boolean isDisplayed() {
			return displayed;
		}
		
		public int getFlagValue() {
			return flag.value;
		}	
		
	}
	
	public static enum EnumHideFlags{
		ENCHANTMENTS(1),
		MODIFIERS(2),
		UNBREAKABLE(4),
		CAN_DESTROY(8),
		CAN_BE_PLACED_ON(16);
		
		private int value;
		
		EnumHideFlags(int value){
			this.value = value;
		}
		
		public static EnumHideFlags getFlagFromValue(int value){
			for(EnumHideFlags flag : values())
				if(value == flag.value)
					return flag;
			return null;
		}
		
		public static int getMaxValue(){
			int max = 0;
			for(EnumHideFlags flag : values())
				if(max < flag.value)
					max = flag.value;
			return max;
		}
		
	}
	
	public static int value(List<HideFlag> flags) {
		int value = 0;
		for (HideFlag flag : flags)
			value += (flag.isDisplayed()) ? flag.getFlagValue() : 0;
		return value;
	}

	public static boolean hasFlag(EnumHideFlags flag, Minecraft mc) {
		if(mc.player.getHeldItemMainhand().getTagCompound() == null)
			return false;
		if(mc.player.getHeldItemMainhand().getTagCompound().getInteger("HideFlags") < flag.value)
			return false;
		String binaryFlag = Integer.toBinaryString(flag.value);
		int index = binaryFlag.length();
		String binaryTag = Integer.toBinaryString(mc.player.getHeldItemMainhand().getTagCompound().getInteger("HideFlags"));
		if(binaryTag.substring(binaryTag.length() - index, binaryTag.length() - index + 1).equals("1"))
			return true;
		return false;
	}

}
