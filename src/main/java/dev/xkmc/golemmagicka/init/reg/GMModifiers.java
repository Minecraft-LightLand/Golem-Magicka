package dev.xkmc.golemmagicka.init.reg;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GMModifiers {

	public static final RegistryEntry<GolemModifier> MANA_REGEN;
	public static final RegistryEntry<GolemModifier> CD_REDUCTION;

	static {
		MANA_REGEN = reg("mana_regen", () -> new AttributeGolemModifier(4,
				new AttributeGolemModifier.AttrEntry(GMTypes.STAT_MANA_REGEN, () -> 0.1)
		), null);
		CD_REDUCTION = reg("cooldown_reduction", () -> new AttributeGolemModifier(4,
				new AttributeGolemModifier.AttrEntry(GMTypes.STAT_CD, () -> 0.1)
		), null);

		//TODO
		//regUpgradeImpl("mana_regen", () -> MANA_REGEN, 1, false, GolemMagicka.MODID);
		//regUpgradeImpl("cooldown_reduction", () -> CD_REDUCTION, 1, false, GolemMagicka.MODID);
	}

	public static <T extends GolemModifier> RegistryEntry<T> reg(String id, NonNullSupplier<T> sup, @Nullable String def) {
		Mutable<RegistryEntry<T>> holder = new MutableObject<>();
		L2Registrate.GenericBuilder<GolemModifier, T> ans = GolemMagicka.REGISTRATE.generic(GolemTypes.MODIFIERS, id, sup).defaultLang();
		if (def != null) {
			ans.addMiscData(ProviderType.LANG, (pvd) -> pvd.add(holder.getValue().get().getDescriptionId() + ".desc", def));
		}
		RegistryEntry<T> result = ans.register();
		holder.setValue(result);
		return result;
	}

	public static ItemBuilder<SimpleUpgradeItem, L2Registrate> regUpgradeImpl(String id, Supplier<RegistryEntry<? extends GolemModifier>> mod, int level, boolean foil, String modid) {
		return GolemMagicka.REGISTRATE.item(id, (p) -> new SimpleUpgradeItem(p, mod.get()::get, level, foil))
				.model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation(modid, "item/upgrades/" + id))
						.override().predicate(new ResourceLocation(ModularGolems.MODID, "blue_arrow"), 0.5f)
						.model(pvd.getBuilder(pvd.name(ctx) + "_purple")
								.parent(new ModelFile.UncheckedModelFile("item/generated"))
								.texture("layer0", new ResourceLocation(modid, "item/upgrades/" + id))
								.texture("layer1", new ResourceLocation(ModularGolems.MODID, "item/purple_arrow")))
						.end().override().predicate(new ResourceLocation(ModularGolems.MODID, "blue_arrow"), 1)
						.model(pvd.getBuilder(pvd.name(ctx) + "_blue")
								.parent(new ModelFile.UncheckedModelFile("item/generated"))
								.texture("layer0", new ResourceLocation(modid, "item/upgrades/" + id))
								.texture("layer1", new ResourceLocation(ModularGolems.MODID, "item/blue_arrow")))
						.end())
				.tab(GolemItems.UPGRADES.getKey());
	}


	public static void register() {

	}

}
