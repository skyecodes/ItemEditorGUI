package com.franckyi.itemeditor.helper;

import java.util.List;

import net.minecraft.client.Minecraft;

public class HideFlagHelper {

	public static class ItemHideFlag {

		private EnumHideFlag flag;
		private boolean displayed;

		public ItemHideFlag(EnumHideFlag flag, boolean displayed) {
			this.flag = flag;
			this.displayed = displayed;
		}

		public EnumHideFlag getFlag() {
			return flag;
		}

		public boolean isDisplayed() {
			return displayed;
		}

		public int getFlagValue() {
			return flag.value;
		}

	}

	public static enum EnumHideFlag {
		ENCHANTMENTS(1, "Hide Enchantments"), MODIFIERS(2, "Hide Attribute Modifiers"), UNBREAKABLE(4,
				"Hide Unbreakable"), CAN_DESTROY(8, "Hide 'Can Destroy'"), CAN_BE_PLACED_ON(16,
						"Hide 'Can Be Placed On'"), POTION_EFFECTS(32, "Hide Potion Effects");

		private int value;
		private String text;

		EnumHideFlag(int value, String text) {
			this.value = value;
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	public static EnumHideFlag getFlagFromValue(int value) {
		for (EnumHideFlag flag : EnumHideFlag.values())
			if (value == flag.value)
				return flag;
		return null;
	}

	public static int value(List<ItemHideFlag> flags) {
		int value = 0;
		for (ItemHideFlag flag : flags)
			value += (flag.isDisplayed()) ? flag.getFlagValue() : 0;
		return value;
	}

	public static boolean hasFlag(EnumHideFlag flag, Minecraft mc) {
		if (ModHelper.clientStack.getTagCompound() == null)
			return false;
		if (ModHelper.clientStack.getTagCompound().getInteger("HideFlags") < flag.value)
			return false;
		String binaryFlag = Integer.toBinaryString(flag.value);
		int index = binaryFlag.length();
		String binaryTag = Integer.toBinaryString(ModHelper.clientStack.getTagCompound().getInteger("HideFlags"));
		if (binaryTag.substring(binaryTag.length() - index, binaryTag.length() - index + 1).equals("1"))
			return true;
		return false;
	}

	public static EnumHideFlag getFlagFromID(int i) {
		for (EnumHideFlag flag : EnumHideFlag.values())
			if (i == flag.ordinal())
				return flag;
		return null;
	}

}
