package dev.xkmc.golemmagicka.content.entity;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;

public record SpellEntry(AbstractSpell spell, int level, CastSource source) {
}
