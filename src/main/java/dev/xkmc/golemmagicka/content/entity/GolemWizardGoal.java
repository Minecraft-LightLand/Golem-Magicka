package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.util.SpellCategoryUtil;
import dev.xkmc.mob_weapon_api.api.goals.IWeaponGoal;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/// Goal for golem to cast spell
public class GolemWizardGoal<E extends AbstractGolemEntity<?, ?>> extends WizardAttackGoal implements IWeaponGoal<E> {

	private final GolemMagicData data;

	private LinkedHashMap<AbstractSpell, SpellEntry> spellCache = null;

	public GolemWizardGoal(GolemMagicData data, IMagicEntity entity, double pSpeedModifier, int pAttackInterval) {
		super(entity, pSpeedModifier, pAttackInterval);
		this.data = data;
	}

	@Override
	protected AbstractSpell getNextSpellType() {
		updateAvailableSpells();
		return super.getNextSpellType();
	}

	@Override
	public boolean mayActivate(ItemStack stack) {
		if (data.isCasting()) return true;
		updateAvailableSpells();
		return !attackSpells.isEmpty() || !defenseSpells.isEmpty() ||
				!movementSpells.isEmpty() || !supportSpells.isEmpty();
	}

	@Override
	public double range(ItemStack stack) {
		return 35;
	}

	@Override
	protected void doSpellAction() {
		AbstractSpell spell = this.getNextSpellType();
		if (spell == SpellRegistry.none()) {
			this.spellAttackDelay = 5;
			return;
		}
		var entry = spellCache.get(spell);
		if (entry == null) {
			spellCache = null;
			this.spellAttackDelay = 2;
			return;
		}
		float cost = spell.getManaCost(entry.level());
		if (spell.getCastType() == CastType.CONTINUOUS) {
			int factor = spell.getCastTime(entry.level()) / 10;
			cost *= factor;
		}
		if (data.getMagicData().getMana() < cost) {
			this.spellAttackDelay = 10;
			return;
		}
		if (!spell.shouldAIStopCasting(entry.level(), this.mob, this.target)) {
			int recast = Math.max(1, spell.getRecastCount(entry.level(), mob));
			int cd = GolemSpellManager.getEffectiveSpellCooldown(spell, data.golem, entry.source()) / recast;
			data.setCastingData(new CastingSpellData(spell, entry.level(), entry.source(), cost / recast, cd));
			this.spellCastingMob.initiateCastSpell(spell, entry.level());
			this.fleeCooldown = 7 + spell.getCastTime(entry.level());
		} else {
			this.spellAttackDelay = 5;
		}
		spellCache = null;
	}

	public void updateAvailableSpells() {
		if (spellCache == null || spellCache.isEmpty()) {
			var spells = SpellCategoryUtil.getSpells(data.golem);
			spellCache = new LinkedHashMap<>();
			for (var e : spells)
				spellCache.put(e.spell(), e);
		}
		List<AbstractSpell> atkSpells = new ArrayList<>();
		List<AbstractSpell> defSpells = new ArrayList<>();
		List<AbstractSpell> movSpells = new ArrayList<>();
		List<AbstractSpell> sptSpells = new ArrayList<>();
		for (var ent : spellCache.values()) {
			var e = ent.spell();
			int mana = e.getManaCost(ent.level());
			if (e.getCastType() == CastType.CONTINUOUS) {
				int factor = e.getCastTime(ent.level()) / 10;
				mana *= factor;
			}
			if (mana > data.getMagicData().getMana())
				continue;
			if (data.getMagicData().getPlayerCooldowns().isOnCooldown(e))
				continue;
			if (SpellCategoryUtil.isSupport(e))
				sptSpells.add(e);
			else if (SpellCategoryUtil.isMovement(e))
				movSpells.add(e);
			else if (SpellCategoryUtil.isDefense(e))
				defSpells.add(e);
			else atkSpells.add(e);
		}
		setSpells(atkSpells, defSpells, movSpells, sptSpells);
	}

}
