package com.franckyi.itemeditor.misc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.enchantment.EnumEnchantmentType;

public class ModEnchantmentHelper {

	public static class ModEnchantment {

		private int ench;
		private int level;

		public ModEnchantment(int ench, int level) {
			this.ench = ench;
			this.level = level;
		}

		public int getEnch() {
			return ench;
		}

		public int getLevel() {
			return level;
		}

	}

	public static class EnchantmentListEntry implements IGuiListEntry {

		private EnumEnchantmentList ench;
		private int level;

		private int index;
		private GuiTextField tf;

		public EnchantmentListEntry(int index, EnumEnchantmentList ench, int level, int widthIn, int heightIn) {
			this.index = index;
			this.ench = ench;
			this.level = level;
			this.tf = new GuiTextField(index, Minecraft.getMinecraft().fontRendererObj, widthIn / 2 + 50, 0, 50, 15);
		}

		public EnchantmentListEntry(int id, int level) {
			this.ench = EnumEnchantmentList.getTypeFromID(id);
			this.level = level;
		}

		public EnumEnchantmentList getEnch() {
			return ench;
		}

		public int getLevel() {
			return level;
		}

		public int getIndex() {
			return index;
		}

		public GuiTextField getTextField() {
			return tf;
		}

		@Override
		public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {

		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
				boolean isSelected) {
			tf.yPosition = y;
			tf.drawTextBox();
			if (ench.id == 10 || ench.id == 71) {
				Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRendererObj, ench.getName(),
						x, y + 5, 0xaa0000);
			} else if (ench.type.canEnchantItem(Minecraft.getMinecraft().player.getHeldItemMainhand().getItem())) {
				Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRendererObj, ench.getName(),
						x, y + 5, 0x00aa00);
			} else {
				Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().fontRendererObj, ench.getName(),
						x, y + 5, 0xffffff);
			}
		}

		@Override
		public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX,
				int relativeY) {
			return false;
		}

		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {

		}

	}

	public static enum EnumEnchantmentList {

		PROTECTION_ALL("Protection", 0, EnumEnchantmentType.ARMOR), PROTECTION_FIRE("Fire Protection", 1,
				EnumEnchantmentType.ARMOR), PROTECTION_FALL("Feather Falling", 2,
						EnumEnchantmentType.ARMOR_FEET), PROTECTION_EXPLOSION("Blast Protection", 3,
								EnumEnchantmentType.ARMOR), PROTECTION_PROJECTILE("Projectile Protection", 4,
										EnumEnchantmentType.ARMOR), OXYGEN("Respiration", 5,
												EnumEnchantmentType.ARMOR_HEAD), WATER_WORKER("Aqua Affinity", 6,
														EnumEnchantmentType.ARMOR_HEAD), THORNS("Thorns", 7,
																EnumEnchantmentType.ARMOR), WATER_WALKER(
																		"Depth Strider", 8,
																		EnumEnchantmentType.ARMOR_FEET), FROST_WALKER(
																				"Frost Walker", 9,
																				EnumEnchantmentType.ARMOR_FEET), BINDING_CURSE(
																						"Curse of Binding", 10,
																						EnumEnchantmentType.WEARABLE),

		DAMAGE_ALL("Sharpness", 16, EnumEnchantmentType.WEAPON), DAMAGE_UNDEAD("Smite", 17,
				EnumEnchantmentType.WEAPON), DAMAGE_ARTHROPODS("Bane of Arthropods", 18,
						EnumEnchantmentType.WEAPON), KNOCKBACK("Knockback", 19,
								EnumEnchantmentType.WEAPON), FIRE_ASPECT("Fire Aspect", 20,
										EnumEnchantmentType.WEAPON), LOOT_BONUS("Looting", 21,
												EnumEnchantmentType.WEAPON), SWEEPING_EDGE("Sweeping Edge", 22,
														EnumEnchantmentType.WEAPON),

		DIGGING("Efficiency", 32, EnumEnchantmentType.DIGGER), UNTOUCHING("Silk Touch", 33,
				EnumEnchantmentType.DIGGER), DURABILITY("Unbreaking", 34,
						EnumEnchantmentType.BREAKABLE), LOOT_BONUS_DIGGER("Fortune", 35, EnumEnchantmentType.DIGGER),

		ARROW_DAMAGE("Power", 48, EnumEnchantmentType.BOW), ARROW_KNOCKBACK("Punch", 49,
				EnumEnchantmentType.BOW), ARROW_FIRE("Flame", 50,
						EnumEnchantmentType.BOW), ARROW_INFINITE("Infinity", 51, EnumEnchantmentType.BOW),

		LOOT_BONUS_FISHING("Luck of the Sea", 61, EnumEnchantmentType.FISHING_ROD), FISHING_SPEED("Lure", 62,
				EnumEnchantmentType.FISHING_ROD),

		MENDING("Mending", 70, EnumEnchantmentType.BREAKABLE), VANISHING_CURSE("Curse of Vanishing", 71,
				EnumEnchantmentType.BREAKABLE);

		private String name;
		private int id;
		private EnumEnchantmentType type;

		private EnumEnchantmentList(String name, int id, EnumEnchantmentType type) {
			this.name = name;
			this.id = id;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public int getId() {
			return id;
		}

		public EnumEnchantmentType getType() {
			return type;
		}

		public static List<EnchantmentListEntry> getDefaults(int widthIn, int heightIn) {
			List<EnchantmentListEntry> enchList = new ArrayList<EnchantmentListEntry>();
			for (EnumEnchantmentList ench : values())
				enchList.add(new EnchantmentListEntry(ench.ordinal(), ench, 0, widthIn, heightIn));
			return enchList;
		}

		public static EnumEnchantmentList getTypeFromID(int id) {
			for (EnumEnchantmentList type : values())
				if (id == type.getId())
					return type;
			return null;
		}

	}

}
