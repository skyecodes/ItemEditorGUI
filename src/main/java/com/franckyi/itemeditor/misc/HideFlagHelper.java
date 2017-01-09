package com.franckyi.itemeditor.misc;

import java.util.List;

import com.franckyi.itemeditor.misc.HideFlagHelper.HideFlag;

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
		
	}
	
	public static int value(List<HideFlag> flags) {
		int value = 0;
		for (HideFlag flag : flags)
			value += (flag.isDisplayed()) ? 0 : flag.getFlagValue();
		return value;
	}

}
