package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.events.GolemCheckSpellEvent;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.golemmagicka.init.data.GMTagGen;
import dev.xkmc.golemmagicka.util.SpellCategoryUtil;
import dev.xkmc.mob_weapon_api.api.goals.IWeaponGoal;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;

/// Goal for golem to cast spell
public class GolemWizardGoal<E extends AbstractGolemEntity<?, ?>> extends WizardAttackGoal implements IWeaponGoal<E> {

	private final GolemMagicData data;

	private LinkedHashMap<AbstractSpell, SpellEntry> spellCache = null;

	public GolemWizardGoal(GolemMagicData data, IMagicEntity entity, double pSpeedModifier, int pAttackInterval) {
		super(entity, pSpeedModifier, pAttackInterval);
		this.data = data;
	}

	public boolean canUse() {
		ItemStack stack = data.golem.getMainHandItem();
		if (GolemSpellManager.predicate(data.golem, stack, InteractionHand.MAIN_HAND).isEmpty())
			return false;
		LivingEntity livingentity = this.mob.getTarget();
		if (livingentity != null && livingentity.isAlive()) {
			if (target != livingentity) {
				data.setNewTarget(target);
			}
			this.target = livingentity;
			return this.mob.canAttack(this.target) && mayActivate(stack);
		} else {
			return false;
		}
	}

	@Override
	protected AbstractSpell getNextSpellType() {
		var opt = updateAvailableSpells().getRandomValue(mob.getRandom());
		return opt.map(SpellEntry::spell).orElseGet(SpellRegistry::none);
	}

	@Override
	public boolean mayActivate(ItemStack stack) {
		if (data.isCasting()) return true;
		return !updateAvailableSpells().isEmpty();
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
		int recast = Math.max(1, spell.getRecastCount(entry.level(), mob));
		float cost = 1f * spell.getManaCost(entry.level()) / recast;
		float totalCost = cost;
		int cd = GolemSpellManager.getEffectiveSpellCooldown(spell, data.golem, entry.source()) / recast;
		if (spell.getCastType() == CastType.CONTINUOUS) {
			int factor = spell.getCastTime(entry.level()) / 10;
			totalCost *= factor;
		}
		if (data.getMagicData().getMana() < totalCost) {
			this.spellAttackDelay = 10;
			return;
		}
		if (!spell.shouldAIStopCasting(entry.level(), this.mob, this.target)) {
			data.setCastingData(new CastingSpellData(spell, entry.level(), entry.source(), cost, cd));
			this.spellCastingMob.initiateCastSpell(spell, entry.level());
			this.fleeCooldown = 7 + spell.getCastTime(entry.level());
			spellcastingRangeSqr = GolemMagicka.SPELL.getMerged().get(spell).getPreferredDistSqr();
		} else {
			this.spellAttackDelay = 5;
		}
		spellCache = null;
	}

	public SimpleWeightedRandomList<SpellEntry> updateAvailableSpells() {
		if (spellCache == null || spellCache.isEmpty()) {
			var spells = SpellCategoryUtil.getSpells(data.golem);
			spellCache = new LinkedHashMap<>();
			for (var e : spells)
				spellCache.put(e.spell(), e);
		}
		SimpleWeightedRandomList.Builder<SpellEntry> builder = new SimpleWeightedRandomList.Builder<>();
		var merged = GolemMagicka.SPELL.getMerged();
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
			if (isUnavailable(e, target))
				continue;
			if (NeoForge.EVENT_BUS.post(new GolemCheckSpellEvent(data.golem, target, data, ent)).isCanceled())
				continue;
			var mem = target == null ? null : data.getMemory(target);
			int weight = merged.get(e).weight(data.golem, target, data.getMagicData(), mana, mem, ent.level());
			if (weight <= 0) continue;
			builder.add(ent, weight);
		}
		return builder.build();
	}

	private boolean isUnavailable(AbstractSpell e, @Nullable LivingEntity target) {
		if (!data.golem.getMode().isMovable()) {
			if (SpellCategoryUtil.is(e, GMTagGen.MOVEMENT)) {
				return true;
			}
		}
		return false;
	}

}
