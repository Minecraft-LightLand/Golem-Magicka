package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.mob_weapon_api.api.goals.IMeleeGoal;
import dev.xkmc.mob_weapon_api.registry.WeaponStatus;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.weapon.GolemWeaponRegistry;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.item.IScroll;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.item.CastingItem;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class GolemSpellManager {

	public static void init() {
		GolemWeaponRegistry.HUMANOID.register(GolemMagicka.loc("iron_spells"),
				GolemSpellManager::predicate, GolemSpellManager::create);
	}

	private static <T extends AbstractGolemEntity<?, ?>> GolemWizardGoal<T> create(T e, IMeleeGoal melee) {
		return new GolemWizardGoal<>(((IGolemMagicka) e).magicka$getGolemMagicData(), ((IMagicEntity) e), 1, 20);
	}

	private static Optional<WeaponStatus> predicate(LivingEntity e, ItemStack stack, @Nullable InteractionHand hand) {
		boolean valid = stack.getItem() instanceof IPresetSpellContainer ||
				stack.getItem() instanceof IScroll ||
				stack.getItem() instanceof CastingItem;
		return WeaponStatus.OFFENSIVE.of(valid);
	}

	public static List<SpellEntry> getSpells(LivingEntity e) {
		return List.of();//TODO
	}

	public static void tickGolemSpellData(AbstractGolemEntity<?, ?> e, MagicData data) {
		data.getPlayerCooldowns().tick(1);
		if (e.tickCount % 10 != 0) return;
		if (e.getAttribute(AttributeRegistry.MAX_MANA.get()) == null) return;
		if (e.getAttribute(AttributeRegistry.MANA_REGEN.get()) == null) return;
		int playerMaxMana = (int) e.getAttributeValue(AttributeRegistry.MAX_MANA.get());
		float mana = data.getMana();
		if (mana != (float) playerMaxMana) {
			float rate = (float) e.getAttributeValue(AttributeRegistry.MANA_REGEN.get());
			float increment = (float) playerMaxMana * 0.01F * rate;
			data.setMana(Mth.clamp(data.getMana() + increment, 0.0F, (float) playerMaxMana));
		}
	}

}
