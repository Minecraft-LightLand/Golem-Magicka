package dev.xkmc.golemmagicka.util;

import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record GolemManaInfo(float mana, float maxMana, float regen, long timestamp) {

	public static GolemManaInfo get(ItemStack stack) {
		return Optional.ofNullable(stack.getTag())
				.filter((e) -> e.contains("golem_entity"))
				.map((e) -> e.getCompound("golem_entity"))
				.map((e) -> new GolemManaInfo(
						e.getFloat("Mana"),
						e.getFloat("MaxMana"),
						e.getFloat("ManaRegen"),
						e.getLong("SpellLastTimeStamp")
				)).orElse(null);
	}

	public int getMana(long time) {
		if (time < timestamp) return (int) mana;
		int diff = (int) ((time - timestamp) / 10);
		return (int) Math.min(maxMana, mana + maxMana * 0.01f * regen * diff);
	}

	public int getMaxMana() {
		return (int) maxMana;
	}

}
