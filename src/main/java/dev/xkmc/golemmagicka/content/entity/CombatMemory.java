package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.golemmagicka.util.SpellCategoryUtil;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class CombatMemory {

	public final LivingEntity target;

	private int attackSpellCount;

	private final Map<MobEffect, EffectMemory> effectMemory = new LinkedHashMap<>();

	public CombatMemory(LivingEntity target) {
		this.target = target;
	}

	public int attackSpellCount() {
		return attackSpellCount;
	}

	public void castSpell(AbstractSpell spell) {
		addMemory(spell);
		if (SpellCategoryUtil.nonOffensive(spell))
			return;
		attackSpellCount++;
	}

	public void addMemory(AbstractSpell spell) {
		var data = GolemMagicka.SPELL.getMerged().get(spell);
		if (data.inflictedEffect != null) {
			var eff = data.inflictedEffect.value();
			if (effectMemory.containsKey(eff)) return;
			effectMemory.put(eff, new EffectMemory(data.inflictedEffect, 20));
		}

	}

	public void tick() {
		for (var e : effectMemory.values()) {
			if (e.time > 0) {
				e.time--;
				if (e.time == 0) {
					if (!target.hasEffect(e.eff)) {
						e.immune = true;
					}
				}
			}
		}
	}

	public boolean canInflict(Holder<MobEffect> effect) {
		var mem = effectMemory.get(effect.value());
		if (mem != null && mem.immune) return false;
		return true;
	}

	private static class EffectMemory {

		private final Holder<MobEffect> eff;
		private int time;
		private boolean immune = false;

		public EffectMemory(Holder<MobEffect> eff, int t) {
			this.eff = eff;
			this.time = t;
		}

	}

}
