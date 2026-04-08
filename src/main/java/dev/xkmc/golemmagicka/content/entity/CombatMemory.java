package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.util.SpellCategoryUtil;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.LivingEntity;

public class CombatMemory {

	public final LivingEntity target;

	private int attackSpellCount;

	public CombatMemory(LivingEntity target) {
		this.target = target;
	}

	public int attackSpellCount() {
		return attackSpellCount;
	}

	public void castSpell(AbstractSpell spell) {
		if (SpellCategoryUtil.nonOffensive(spell))
			return;
		attackSpellCount++;
	}

}
