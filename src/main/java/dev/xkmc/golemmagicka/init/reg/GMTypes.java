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
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class GMTypes {

	private static final ResourceKey<Registry<GolemStatType>> STAT_TYPES =
			ResourceKey.createRegistryKey(new ResourceLocation(ModularGolems.MODID, "stat_type"));

	public static final RegistryEntry<GolemStatType> STAT_MAX_MANA;
	public static final RegistryEntry<GolemStatType> STAT_MANA_REGEN;
	public static final RegistryEntry<GolemStatType> STAT_SPELL_POWER;
	public static final RegistryEntry<GolemStatType> STAT_CD;
	public static final RegistryEntry<GolemStatType> STAT_RESIST;


	public static final RegistryEntry<GolemStatType>
			FIRE_SPELL_POWER, ICE_SPELL_POWER, LIGHTNING_SPELL_POWER, HOLY_SPELL_POWER,
			ENDER_SPELL_POWER, BLOOD_SPELL_POWER, EVOCATION_SPELL_POWER,
			NATURE_SPELL_POWER, ELDRITCH_SPELL_POWER;

	public static final RegistryEntry<GolemStatType>
			FIRE_RESIST, ICE_RESIST, LIGHTNING_RESIST, HOLY_RESIST,
			ENDER_RESIST, BLOOD_RESIST, EVOCATION_RESIST,
			NATURE_RESIST, ELDRITCH_RESIST;

	static {
		STAT_MAX_MANA = regStatPlain("max_mana", () -> AttributeRegistry.MAX_MANA, GolemStatType.Kind.ADD, StatFilterType.MASS);
		STAT_MANA_REGEN = regStat("mana_regen", () -> AttributeRegistry.MANA_REGEN, GolemStatType.Kind.ADD, StatFilterType.MASS);
		STAT_SPELL_POWER = regStat("spell_power", () -> AttributeRegistry.SPELL_POWER, GolemStatType.Kind.ADD, StatFilterType.ATTACK);
		STAT_CD = regStat("spell_cooldown", () -> AttributeRegistry.COOLDOWN_REDUCTION, GolemStatType.Kind.ADD, StatFilterType.ATTACK);
		STAT_RESIST = regStat("spell_resist", () -> AttributeRegistry.SPELL_RESIST, GolemStatType.Kind.ADD, StatFilterType.HEALTH);

		FIRE_SPELL_POWER = regStat("fire_spell_power", () -> AttributeRegistry.FIRE_SPELL_POWER, GolemStatType.Kind.ADD, StatFilterType.ATTACK);
		ICE_SPELL_POWER = regStat("ice_spell_power", () -> AttributeRegistry.ICE_SPELL_POWER, GolemStatType.Kind.ADD, StatFilterType.ATTACK);
		LIGHTNING_SPELL_POWER = regStat("lightning_spell_power", () -> AttributeRegistry.LIGHTNING_SPELL_POWER, GolemStatType.Kind.ADD, StatFilterType.ATTACK);
		HOLY_SPELL_POWER = regStat("holy_spell_power", () -> AttributeRegistry.HOLY_SPELL_POWER, GolemStatType.Kind.ADD, StatFilterType.ATTACK);
		ENDER_SPELL_POWER = regStat("ender_spell_power", () -> AttributeRegistry.ENDER_SPELL_POWER, GolemStatType.Kind.ADD, StatFilterType.ATTACK);
		BLOOD_SPELL_POWER = regStat("blood_spell_power", () -> AttributeRegistry.BLOOD_SPELL_POWER, GolemStatType.Kind.ADD, StatFilterType.ATTACK);
		EVOCATION_SPELL_POWER = regStat("evocation_spell_power", () -> AttributeRegistry.EVOCATION_SPELL_POWER, GolemStatType.Kind.ADD, StatFilterType.ATTACK);
		NATURE_SPELL_POWER = regStat("nature_spell_power", () -> AttributeRegistry.NATURE_SPELL_POWER, GolemStatType.Kind.ADD, StatFilterType.ATTACK);
		ELDRITCH_SPELL_POWER = regStat("eldritch_spell_power", () -> AttributeRegistry.ELDRITCH_SPELL_POWER, GolemStatType.Kind.ADD, StatFilterType.ATTACK);

		FIRE_RESIST = regStat("fire_resist", () -> AttributeRegistry.FIRE_MAGIC_RESIST, GolemStatType.Kind.ADD, StatFilterType.HEALTH);
		ICE_RESIST = regStat("ice_resist", () -> AttributeRegistry.ICE_MAGIC_RESIST, GolemStatType.Kind.ADD, StatFilterType.HEALTH);
		LIGHTNING_RESIST = regStat("lightning_resist", () -> AttributeRegistry.LIGHTNING_MAGIC_RESIST, GolemStatType.Kind.ADD, StatFilterType.HEALTH);
		HOLY_RESIST = regStat("holy_resist", () -> AttributeRegistry.HOLY_MAGIC_RESIST, GolemStatType.Kind.ADD, StatFilterType.HEALTH);
		ENDER_RESIST = regStat("ender_resist", () -> AttributeRegistry.ENDER_MAGIC_RESIST, GolemStatType.Kind.ADD, StatFilterType.HEALTH);
		BLOOD_RESIST = regStat("blood_resist", () -> AttributeRegistry.BLOOD_MAGIC_RESIST, GolemStatType.Kind.ADD, StatFilterType.HEALTH);
		EVOCATION_RESIST = regStat("evocation_resist", () -> AttributeRegistry.EVOCATION_MAGIC_RESIST, GolemStatType.Kind.ADD, StatFilterType.HEALTH);
		NATURE_RESIST = regStat("nature_resist", () -> AttributeRegistry.NATURE_MAGIC_RESIST, GolemStatType.Kind.ADD, StatFilterType.HEALTH);
		ELDRITCH_RESIST = regStat("eldritch_resist", () -> AttributeRegistry.ELDRITCH_MAGIC_RESIST, GolemStatType.Kind.ADD, StatFilterType.HEALTH);

	}

	private static RegistryEntry<GolemStatType> regStatPlain(String id, Supplier<RegistryObject<Attribute>> sup, GolemStatType.Kind kind, StatFilterType type) {
		return GolemMagicka.REGISTRATE.generic(id, STAT_TYPES, () -> new GolemStatType(sup.get(), kind, type)).register();
	}

	private static RegistryEntry<GolemStatType> regStat(String id, Supplier<RegistryObject<Attribute>> sup, GolemStatType.Kind kind, StatFilterType type) {
		return GolemMagicka.REGISTRATE.generic(id, STAT_TYPES, () -> new GolemStatType(sup.get(), kind, type, true)).register();
	}


	public static void register() {

	}

}
