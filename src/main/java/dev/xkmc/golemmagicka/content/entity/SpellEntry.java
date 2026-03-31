package dev.xkmc.golemmagicka.content.entity;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;

public record SpellEntry(AbstractSpell spell, int level) {
}
