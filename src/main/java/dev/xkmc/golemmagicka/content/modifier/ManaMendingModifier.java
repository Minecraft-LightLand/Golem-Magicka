package dev.xkmc.golemmagicka.content.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;

public class ManaMendingModifier extends GolemModifier {

	public ManaMendingModifier() {
		super(StatFilterType.HEALTH, 1);
	}

}
