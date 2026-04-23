package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.golemmagicka.init.data.GMConfig;
import dev.xkmc.golemmagicka.init.reg.GMModifiers;
import dev.xkmc.mob_weapon_api.api.goals.IMeleeGoal;
import dev.xkmc.mob_weapon_api.registry.WeaponStatus;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.weapon.GolemWeaponRegistry;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.CastingItem;
import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GolemSpellManager {

	public static void init() {
		GolemWeaponRegistry.HUMANOID.register(GolemMagicka.loc("iron_spells"),
				GolemSpellManager::predicate, GolemSpellManager::create);
		GolemWeaponRegistry.LARGE.register(GolemMagicka.loc("iron_spells"),
				GolemSpellManager::predicate, GolemSpellManager::create);
	}

	private static <T extends AbstractGolemEntity<?, ?>> GolemWizardGoal<T> create(T e, IMeleeGoal melee) {
		return new GolemWizardGoal<>(((IGolemMagicka) e).magicka$getGolemMagicData(), ((IMagicEntity) e), 1, 20);
	}

	public static Optional<WeaponStatus> predicate(LivingEntity e, ItemStack stack, @Nullable InteractionHand hand) {
		boolean valid = stack.getItem() instanceof SpellBook ||
				stack.getItem() instanceof CastingItem ||
				stack.getItem() instanceof MagicSwordItem ||
				ISpellContainer.isSpellContainer(stack);
		return WeaponStatus.OFFENSIVE.withPriority(1000).of(valid);
	}

	public static void tickGolemSpellData(AbstractGolemEntity<?, ?> e, MagicData data) {
		data.getPlayerCooldowns().tick(1);
		if (e.level().isClientSide()) return;
		if (e.tickCount % 10 != 0) return;
		if (e.getAttribute(AttributeRegistry.MAX_MANA.get()) == null) return;
		if (e.getAttribute(AttributeRegistry.MANA_REGEN.get()) == null) return;
		int maxMana = (int) e.getAttributeValue(AttributeRegistry.MAX_MANA.get());
		float rate = (float) e.getAttributeValue(AttributeRegistry.MANA_REGEN.get());
		float increment = maxMana * 0.01F * rate;
		for (var p : e.getPassengers()) {
			if (!(p instanceof LivingEntity passenger)) continue;
			if (passenger.getAttribute(AttributeRegistry.MAX_MANA.get()) == null) continue;
			MagicData riderData = null;
			if (passenger instanceof AbstractGolemEntity<?, ?> rider) {
				riderData = ((IMagicEntity) rider).getMagicData();
			} else if (passenger instanceof ServerPlayer sp) {
				riderData = MagicData.getPlayerMagicData(sp);
			}
			if (riderData == null) continue;
			float diff = (float) passenger.getAttributeValue(AttributeRegistry.MAX_MANA.get()) - riderData.getMana();
			if (diff <= 0) continue;
			int max = (int) Math.min(diff, Math.min(increment * 2, (data.getMana() - 100) / 2));
			if (max > 0) {
				data.addMana(-max);
				riderData.addMana(max);
			}
		}
		float mana = data.getMana();
		int lv = e.getModifiers().get(GMModifiers.MANA_MENDING.get());
		if (lv > 0) {
			float hp = e.getHealth();
			float mhp = e.getMaxHealth();
			if (hp / mhp < mana / maxMana) {
				float f = GMConfig.COMMON.manaMendingRate.get().floatValue();
				e.heal(increment / f);
				float nhp = e.getHealth();
				increment -= (nhp - hp) * f;
				if (increment < 0) return;
			}
		}
		data.setMana(Mth.clamp(data.getMana() + increment, 0, maxMana));
		GolemSpellInfoToClient.send(e, data.getMana());
	}

	public static int getEffectiveSpellCooldown(AbstractSpell spell, LivingEntity e, CastSource source) {
		double rate = e.getAttributeValue(AttributeRegistry.COOLDOWN_REDUCTION.get());
		float factor = 1.0F;
		if (source == CastSource.SWORD) {
			factor = ServerConfigs.SWORDS_CD_MULTIPLIER.get().floatValue();
		}
		return (int) (spell.getSpellCooldown() * (2 - Utils.softCapFormula(rate)) * factor);
	}

}
