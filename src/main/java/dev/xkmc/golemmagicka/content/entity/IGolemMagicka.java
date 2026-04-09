package dev.xkmc.golemmagicka.content.entity;

import io.redspace.ironsspellbooks.api.magic.MagicData;

public interface IGolemMagicka extends MagicData.IExtendedEntity{

	GolemMagicData magicka$getGolemMagicData();

	@Override
	MagicData irons_spellbooks$getMagicData();
}
