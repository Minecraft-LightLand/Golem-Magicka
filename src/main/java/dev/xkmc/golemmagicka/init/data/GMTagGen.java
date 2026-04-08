package dev.xkmc.golemmagicka.init.data;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Objects;

public class GMTagGen {

	public static final ProviderType<RegistrateTagsProvider.IntrinsicImpl<AbstractSpell>> SPELL_TAGS =
			ProviderType.registerIntrinsicTag("tags/spell", "spell",
					SpellRegistry.SPELL_REGISTRY_KEY, (spell) ->
							ResourceKey.create(SpellRegistry.SPELL_REGISTRY_KEY,
									Objects.requireNonNull(SpellRegistry.REGISTRY.getKey(spell))));

	public static final TagKey<AbstractSpell> WHITELIST = loc("whitelist");
	public static final TagKey<AbstractSpell> BLACKLIST = loc("blacklist");
	public static final TagKey<AbstractSpell> DEFENSE = loc("defense");
	public static final TagKey<AbstractSpell> MOVEMENT = loc("movement");
	public static final TagKey<AbstractSpell> ENHANCE = loc("enhance");
	public static final TagKey<AbstractSpell> CONTROL = loc("control");
	public static final TagKey<AbstractSpell> SUPPORT = loc("support");
	public static final TagKey<AbstractSpell> SUMMON = loc("summon");
	public static final TagKey<AbstractSpell> NON_OFFENSIVE = loc("non_offensive");

	public static void genSpellTag(RegistrateTagsProvider.IntrinsicImpl<AbstractSpell> pvd) {
		pvd.addTag(WHITELIST).add(SpellRegistry.ELDRITCH_BLAST_SPELL.get());
		pvd.addTag(BLACKLIST).add(
				SpellRegistry.POISON_SPLASH_SPELL.get(),
				SpellRegistry.TELEKINESIS_SPELL.get(),
				SpellRegistry.POCKET_DIMENSION_SPELL.get(),
				SpellRegistry.SPECTRAL_HAMMER_SPELL.get(),
				SpellRegistry.ANGEL_WINGS_SPELL.get(),
				SpellRegistry.PORTAL_SPELL.get(),
				SpellRegistry.SUMMON_ENDER_CHEST_SPELL.get(),
				SpellRegistry.RECALL_SPELL.get(),
				SpellRegistry.WALL_OF_FIRE_SPELL.get()
		);
		pvd.addTag(DEFENSE);
		pvd.addTag(MOVEMENT);
		pvd.addTag(ENHANCE);
		pvd.addTag(CONTROL);
		pvd.addTag(SUPPORT);
		pvd.addTag(SUMMON);
		pvd.addTag(NON_OFFENSIVE).addTags(ENHANCE, CONTROL, SUPPORT);
	}

	public static TagKey<AbstractSpell> loc(String id) {
		return TagKey.create(SpellRegistry.SPELL_REGISTRY_KEY, GolemMagicka.loc(id));
	}

}
