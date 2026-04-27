package dev.xkmc.golemmagicka.content.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.content.modifier.special.EarthquakeHelper;

public class PyriumJumpModifier extends GolemModifier implements EarthquakeHelper.Modifier {

	public PyriumJumpModifier() {
		super(StatFilterType.HEALTH, 2);
	}

	@Override
	public void performEarthQuake(AbstractGolemEntity<?, ?> abstractGolemEntity, int i) {
		//TODO
	}

}
