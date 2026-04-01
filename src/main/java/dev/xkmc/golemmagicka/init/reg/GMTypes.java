package dev.xkmc.golemmagicka.init.reg;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.init.ModularGolems;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.function.Supplier;

public class GMTypes {

	private static final ResourceKey<Registry<GolemStatType>> STAT_TYPES =
			ResourceKey.createRegistryKey(new ResourceLocation(ModularGolems.MODID, "stat_type"));

	public static final RegistryEntry<GolemStatType> STAT_MAX_MANA;
	public static final RegistryEntry<GolemStatType> STAT_MANA_REGEN;
	public static final RegistryEntry<GolemStatType> STAT_SPELL_POWER;

	static {
		STAT_MAX_MANA = regStat("max_mana", () -> AttributeRegistry.MAX_MANA.get(), GolemStatType.Kind.ADD, StatFilterType.MASS);
		STAT_MANA_REGEN = regStat("mana_regen", () -> AttributeRegistry.MANA_REGEN.get(), GolemStatType.Kind.ADD, StatFilterType.MASS);
		STAT_SPELL_POWER = regStat("spell_power", () -> AttributeRegistry.SPELL_POWER.get(), GolemStatType.Kind.ADD, StatFilterType.MASS);


	}

	private static RegistryEntry<GolemStatType> regStat(String id, Supplier<Attribute> sup, GolemStatType.Kind kind, StatFilterType type) {
		return GolemMagicka.REGISTRATE.generic(id, STAT_TYPES, () -> new GolemStatType(sup, kind, type)).register();
	}


	public static void register() {

	}

}
