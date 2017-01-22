package com.franckyi.itemeditor.helper;

import java.util.ArrayList;
import java.util.List;

import com.franckyi.itemeditor.api.gui.GuiIntTextField;
import com.franckyi.itemeditor.client.gui.child.GuiEnchantList.EnchantmentListEntry;

import net.minecraft.enchantment.EnumEnchantmentType;

public class EnchantmentHelper {

	public static class ItemEnchantment {

		private int ench, level;
		private GuiIntTextField textField;

		public ItemEnchantment(int ench, int level) {
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

	public static enum EnumEnchantment {

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

		private EnumEnchantment(String name, int id, EnumEnchantmentType type) {
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
			for (EnumEnchantment ench : values())
				enchList.add(new EnchantmentListEntry(ench.ordinal(), ench, widthIn, heightIn));
			return enchList;
		}

		public static EnumEnchantment getTypeFromID(int id) {
			for (EnumEnchantment type : values())
				if (id == type.getId())
					return type;
			return null;
		}

	}

}
