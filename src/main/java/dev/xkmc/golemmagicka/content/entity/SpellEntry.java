package dev.xkmc.golemmagicka.content.entity;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.world.item.ItemStack;

public record SpellEntry(AbstractSpell spell, int level, CastSource source, ItemStack stack) {
}
