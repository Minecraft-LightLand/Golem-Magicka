package dev.xkmc.golemmagicka.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;

public class WeaponUtil {

	/**
	 * Returns the attack damage if user switch from ItemStack denoted by remove to ItemStack denoted by stack
	 */
	public static double getWeaponAttack(AttributeInstance attr, ItemStack stack, LivingEntity le, ItemStack remove) {
		double base = attr.getBaseValue();
		double add = 0, multBase = 1, multTotal = 1;
		LinkedHashMap<ResourceLocation, AttributeModifier> old = new LinkedHashMap<>();
		remove.getAttributeModifiers().forEach(EquipmentSlot.MAINHAND, (holder, mod) -> {
			if (holder.value() == Attributes.ATTACK_DAMAGE.value()) {
				old.put(mod.id(), mod);
			}
		});
		for (var e : attr.getModifiers()) {
			if (old.containsKey(e.id())) continue;
			switch (e.operation()) {
				case ADD_VALUE -> add += e.amount();
				case ADD_MULTIPLIED_BASE -> multBase += e.amount();
				case ADD_MULTIPLIED_TOTAL -> multTotal *= 1 + e.amount();
			}
		}
		LinkedHashMap<ResourceLocation, AttributeModifier> item = new LinkedHashMap<>();
		stack.getAttributeModifiers().forEach(EquipmentSlot.MAINHAND, (holder, mod) -> {
			if (holder.value() == Attributes.ATTACK_DAMAGE.value()) {
				item.put(mod.id(), mod);
			}
		});
		for (var e : item.values()) {
			switch (e.operation()) {
				case ADD_VALUE -> add += e.amount();
				case ADD_MULTIPLIED_BASE -> multBase += e.amount();
				case ADD_MULTIPLIED_TOTAL -> multTotal *= 1 + e.amount();
			}
		}
		return (base + add) * multBase * multTotal;
	}


}
