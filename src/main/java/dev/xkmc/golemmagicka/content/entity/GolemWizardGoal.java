package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.util.SpellCategoryUtil;
import dev.xkmc.mob_weapon_api.api.goals.IWeaponGoal;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/// Goal for golem to cast spell
///
/// Place to add hooks only. Should not contain any custom logic
public class GolemWizardGoal<E extends AbstractGolemEntity<?, ?>> extends WizardAttackGoal implements IWeaponGoal<E> {

	private final GolemMagicData data;

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
		return super.getNextSpellType() != SpellRegistry.none();
	}

	@Override
	public double range(ItemStack stack) {
		return 20;
	}

	@Override
	public void tick() {
		super.tick();
	}

	public void updateAvailableSpells() {
		var allSpells = GolemSpellManager.getSpells(data.golem);
		List<AbstractSpell> atkSpells = new ArrayList<>();
		List<AbstractSpell> defSpells = new ArrayList<>();
		List<AbstractSpell> movSpells = new ArrayList<>();
		List<AbstractSpell> sptSpells = new ArrayList<>();
		for (var ent : allSpells) {
			var e = ent.spell();
			int mana = e.getManaCost(ent.level());
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
