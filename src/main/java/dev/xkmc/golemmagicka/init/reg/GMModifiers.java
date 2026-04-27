package dev.xkmc.golemmagicka.init.reg;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.golemmagicka.content.modifier.ManaMendingModifier;
import dev.xkmc.golemmagicka.content.modifier.PyriumAttackModifier;
import dev.xkmc.golemmagicka.content.modifier.PyriumJumpModifier;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nullable;

public class GMModifiers {

	public static final Val<ManaMendingModifier> MANA_MENDING = reg("mana_mending", ManaMendingModifier::new, "Mana Mending", "Use mana regen to repair itself when health percentage is lower than mana percentage");
	public static final Val<PyriumAttackModifier> PYRIUM_ATTACK = reg("pyrium_attack", PyriumAttackModifier::new, "Pyrium Attack", "Pyrium Attack");
	public static final Val<PyriumJumpModifier> PYRIUM_JUMP = reg("pyrium_jump", PyriumJumpModifier::new, "Pyrium Jump", "Pyrium Jump");

	public static <T extends GolemModifier> Val<T> reg(String id, NonNullSupplier<T> sup, @Nullable String name, @Nullable String def) {
		Mutable<RegistryEntry<GolemModifier, T>> holder = new MutableObject<>();
		L2Registrate.GenericBuilder<GolemModifier, T> ans = GolemMagicka.REGISTRATE.generic(GolemTypes.MODIFIERS, id, sup).defaultLang();
		if (name != null) {
			ans.lang(NamedEntry::getDescriptionId, name);
		}

		if (def != null) {
			ans.getOwner().addRawLang("modifier." + GolemMagicka.MODID + "." + id + ".desc", def);
		}

		RegistryEntry<GolemModifier, T> result = ans.register();
		holder.setValue(result);
		return new Val.Registrate<>(result);
	}

	public static void register() {

	}

}
