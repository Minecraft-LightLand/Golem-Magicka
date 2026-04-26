package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.events.GolemCheckSpellEvent;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.golemmagicka.init.data.GMTagGen;
import dev.xkmc.golemmagicka.util.SpellCategoryUtil;
import dev.xkmc.golemmagicka.util.WeaponUtil;
import dev.xkmc.mob_weapon_api.api.goals.IMeleeGoal;
import dev.xkmc.mob_weapon_api.api.goals.IWeaponGoal;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.SweepGolemEntity;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.function.Predicate;

/// Goal for golem to cast spell
public class GolemWizardGoal<E extends AbstractGolemEntity<?, ?>> extends WizardAttackGoal implements IWeaponGoal<E> {

	private final GolemMagicData data;
	private final IMeleeGoal melee;

	private LinkedHashMap<AbstractSpell, SpellEntry> spellCache = null;

	public GolemWizardGoal(GolemMagicData data, IMagicEntity entity, IMeleeGoal melee, double pSpeedModifier, int pAttackInterval) {
		super(entity, pSpeedModifier, pAttackInterval);
		this.data = data;
		this.melee = melee;
	}

	public boolean canUse() {
		return canUse(false);
	}

	public boolean canUse(boolean simulate) {
		LivingEntity le = mob.getTarget();
		if (data.isCasting()) {
			if (le != null && le.isAlive() && mob.canAttack(le)) {
				target = le;
				data.setNewTarget(target);
			}
			return true;
		}
		if (le == null || !le.isAlive()) {
			target = null;
			return !updateAvailableSpells(simulate).isEmpty();
		}
		if (!mob.canAttack(le))
			return false;
		if (target != le)
			data.setNewTarget(target);
		target = le;
		return !updateAvailableSpells(simulate).isEmpty();
	}

	@Override
	protected AbstractSpell getNextSpellType() {
		var opt = updateAvailableSpells(false).getRandomValue(mob.getRandom());
		return opt.map(SpellEntry::spell).orElseGet(SpellRegistry::none);
	}

	@Override
	public boolean shouldUseForMelee(ItemStack other) {
		// has spell: always switch in
		if (canUse(false)) return true;
		// no spell, don't switch in
		if (mob.getMainHandItem() == other) return false;
		// switching out
		return !WeaponUtil.isBetterWeapon(mob, other, mob.getMainHandItem());
	}

	@Override
	public boolean mayActivate(ItemStack stack) {
		return canUse(false);
	}

	@Override
	public boolean isAvailable(ItemStack stack) {
		return canUse(true);
	}

	@Override
	public void start() {
		if (target != null && target.isAlive()) {
			mob.setAggressive(true);
		}
	}

	@Override
	public boolean canContinueToUse() {
		ItemStack stack = data.golem.getMainHandItem();
		if (GolemSpellManager.predicate(data.golem, stack, InteractionHand.MAIN_HAND).isEmpty())
			return false;
		if (super.canContinueToUse()) {
			mob.setAggressive(data.isCasting() || target != null && target.isAlive());
			return true;
		}
		return false;
	}

	@Override
	public double range(ItemStack stack) {
		return 35;
	}

	@Override
	public void tick() {
		if (target != null) {
			super.tick();
		} else {
			spellAttackDelay--;
			if (spellAttackDelay == 0) {
				if (!spellCastingMob.isCasting()) {
					doSpellAction();
				}
			}
			if (spellAttackDelay <= 0) {
				resetSpellAttackTimer(0);
			}
		}
	}

	@Override
	protected void doSpellAction() {
		AbstractSpell spell = getNextSpellType();
		if (spell == SpellRegistry.none()) {
			spellAttackDelay = 5;
			return;
		}
		var entry = spellCache.get(spell);
		if (entry == null) {
			spellCache = null;
			spellAttackDelay = 2;
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
			spellAttackDelay = 10;
			return;
		}
		if (target == null || !spell.shouldAIStopCasting(entry.level(), mob, target)) {
			switchTo(entry);
			data.setCastingData(new CastingSpellData(spell, entry.level(), entry.source(), cost, cd));
			spellCastingMob.initiateCastSpell(spell, entry.level());
			fleeCooldown = 7 + spell.getCastTime(entry.level());
			spellcastingRangeSqr = GolemMagicka.SPELL.getMerged().get(spell).getPreferredDistSqr();
		} else {
			spellAttackDelay = 5;
		}
		spellCache = null;
	}

	private void switchTo(SpellEntry entry) {
		if (entry.source() == CastSource.SWORD) {
			switchTo(e -> e == entry.stack());
		} else if (SpellCategoryUtil.is(entry.spell(), GMTagGen.MELEE_ATTACK_SPELL)) {//TODO switch to attribute spell
			ItemStack stack = mob.getMainHandItem();
			switchTo(e -> SpellCategoryUtil.isBetterSpellWeapon(mob, e, stack));
		} else {
			switchTo(e -> e.getItem() instanceof StaffItem);
		}
	}

	private void switchTo(Predicate<ItemStack> pred) {
		ItemStack stack = mob.getMainHandItem();
		if (pred.test(stack)) return;
		if (pred.test(mob.getOffhandItem())) {
			mob.setItemInHand(InteractionHand.MAIN_HAND, mob.getOffhandItem());
			mob.setItemInHand(InteractionHand.OFF_HAND, stack);
			return;
		}
		if (data.golem instanceof SweepGolemEntity<?, ?> sweep) {
			var backup = sweep.getBackupHand();
			if (pred.test(backup.getItem())) {
				mob.setItemInHand(InteractionHand.MAIN_HAND, backup.getItem());
				backup.setItem(stack);
			}
		}
	}

	public SimpleWeightedRandomList<SpellEntry> updateAvailableSpells(boolean simulate) {
		@Nullable var target = this.target;
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
			if (!isAvailable(e, target, simulate))
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

	private boolean isAvailable(AbstractSpell e, @Nullable LivingEntity target, boolean simulate) {
		if (target == null) {
			if (SpellCategoryUtil.is(e, GMTagGen.NO_TARGET))
				return true;
			if (SpellCategoryUtil.is(e, GMTagGen.ENHANCE) || SpellCategoryUtil.is(e, GMTagGen.SUPPORT)) {
				var max = data.golem.getAttributeValue(AttributeRegistry.MAX_MANA);
				var mana = data.getMagicData().getMana();
				if (mana > max - 1)
					return true;
			}
			return false;
		}
		if (!simulate && melee.canReachTarget(target)) {
			if (!SpellCategoryUtil.is(e, GMTagGen.MELEE_SPELL)) {
				return false;
			}
		}
		if (!data.golem.getMode().isMovable()) {
			if (SpellCategoryUtil.is(e, GMTagGen.MOVEMENT)) {
				return false;
			}
		}
		return true;
	}

}
