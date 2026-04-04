package dev.xkmc.golemmagicka.init.data;

import dev.xkmc.golemmagicka.content.config.MagicStatBuilder;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.golemmagicka.init.reg.GMTypes;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.config.GolemPartConfig;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class GMConfigGen extends ConfigDataProvider {

	public GMConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "Golem Spawn Config");
	}

	public void add(ConfigDataProvider.Collector map) {

		map.add(ModularGolems.PARTS, GolemMagicka.loc("magic"), new GolemPartConfig()
				.addEntity(GolemTypes.TYPE_GOLEM.get())
				.addFilter(GMTypes.STAT_MAX_MANA.get(), 2)
				.end()

				.addEntity(GolemTypes.TYPE_DOG.get())
				.addFilter(GMTypes.STAT_MAX_MANA.get(), 0.5)
				.addFilter(GMTypes.STAT_CD.get(), 0)
				.addFilter(GMTypes.STAT_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.FIRE_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.ICE_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.LIGHTNING_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.HOLY_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.ENDER_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.BLOOD_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.EVOCATION_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.NATURE_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.ELDRITCH_SPELL_POWER.get(), 0)
				.end()

		);

		map.add(ModularGolems.MATERIALS, GolemMagicka.loc("vanilla"), new MagicStatBuilder()
				.add(ModularGolems.loc("gold"), 100, 0.4)
				.add(ModularGolems.loc("netherite"), 200, 0.4)
				.add(ModularGolems.loc("sculk"), 100, 0.2)
				.add(ModularGolems.loc("sculk"), GMTypes.ELDRITCH_SPELL_POWER.get(), 0.4)
				.build()
		);

		map.add(ModularGolems.MATERIALS, GolemMagicka.loc("iron_spells"), new GolemMaterialConfig()
				.addMaterial(GolemMagicka.loc("pyrium"), Ingredient.of(ItemRegistry.PYRIUM_INGOT.get()))
				.addStat(GolemTypes.STAT_HEALTH.get(), 400)
				.addStat(GolemTypes.STAT_ATTACK.get(), 30)
				.addStat(GolemTypes.STAT_SWEEP.get(), 1)
				.addStat(GMTypes.STAT_MAX_MANA.get(), 800)
				.addStat(GMTypes.STAT_MANA_REGEN.get(), 1)
				.addStat(GMTypes.STAT_SPELL_POWER.get(), 0.5)
				.addStat(GMTypes.STAT_CD.get(), 0.4)
				.addModifier(GolemModifiers.DAMAGE_CAP.get(), 1)
				.addModifier(GolemModifiers.FIRE_IMMUNE.get(), 1)
				.end()
		);

	}

}
