package com.franckyi.itemeditor.helper;

import com.franckyi.itemeditor.gui.child.IEnumButtonField;
import com.franckyi.itemeditor.helper.AttributeHelper.EnumAttribute;

public class AttributeHelper {

	public static class ItemAttribute {

		private EnumAttribute attribute;
		private double amount;
		private int operation;
		private String slot;

		public ItemAttribute(EnumAttribute attribute, double amount, int operation, String slot) {
			this.attribute = attribute;
			this.amount = amount;
			this.operation = operation;
			this.slot = slot;
		}

		public EnumAttribute getAttribute() {
			return attribute;
		}

		public double getAmount() {
			return amount;
		}

		public int getOperation() {
			return operation;
		}

		public String getSlot() {
			return slot;
		}
		
	}

	public static enum EnumAttribute {
		MAX_HEALTH("generic.maxHealth", "Max Health"), FOLLOW_RANGE("generic.followRange", "Follow Range"), KNOCKBACK_RESISTANCE(
				"generic.knockbackResistance", "Knockback Res."), MOVEMENT_SPEED("generic.movementSpeed", "Movement Speed"), ATTACK_DAMAGE("generic.attackDamage", "Attack Damage");

		private String name, text;

		EnumAttribute(String name, String text) {
			this.name = name;
			this.text = text;
		}

		public String getName() {
			return name;
		}
		
		public String getText() {
			return text;
		}
		
	}
	
	public static EnumAttribute getAttributeFromName(String name){
		for(EnumAttribute attr : EnumAttribute.values())
			if(attr.name.equals(name))
				return attr;
		return null;
	}

	public static EnumAttribute getAttributeFromID(int i) {
		for(EnumAttribute attr : EnumAttribute.values())
			if(i == attr.ordinal())
				return attr;
		return null;
	}
	
	public static enum EnumAttributeOperation implements IEnumButtonField<Integer>{
		ADDITIVE("Additive (0)", 0), MULTIPLICATIVE1("Multiplicative (1)", 1), MULTIPLICATIVE2("Multiplicative (2)", 2);
		
		private String text;
		private int value;

		EnumAttributeOperation(String text, int value) {
			this.text = text;
			this.value = value;
		}

		@Override
		public String getButtonText() {
			return text;
		}

		@Override
		public Integer getButtonValue() {
			return value;
		}
	}
	
	public static enum EnumAttributeSlot implements IEnumButtonField<String>{
		ANY("Any", "any"), MAINHAND("Main Hand", "mainhand"), OFFHAND("Off Hand", "offhand"), HELMET("Helmet", "head"), CHESTPLATE("Chestplate", "chest"), LEGGINGS("Leggings", "legs"), BOOTS("Boots", "feet");

		private String text;
		private String value;
		
		EnumAttributeSlot(String text, String value) {
			this.text = text;
			this.value = value;
		}
		
		@Override
		public String getButtonText() {
			return text;
		}

		@Override
		public String getButtonValue() {
			return value;
		}
	}

}
