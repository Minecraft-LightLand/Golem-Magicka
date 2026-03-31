package dev.xkmc.golemmagicka.content.config;

import dev.xkmc.golemmagicka.init.reg.GMTypes;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;

public class MagicStatBuilder {

	private final GolemMaterialConfig ans = new GolemMaterialConfig();

	public MagicStatBuilder add(ResourceLocation mat, double max, double regen) {
		LinkedHashMap<GolemStatType, Double> map = new LinkedHashMap<>();
		map.put(GMTypes.STAT_MAX_MANA.get(), max);
		map.put(GMTypes.STAT_MANA_REGEN.get(), regen);
		ans.stats.put(mat, map);
		return this;
	}

	public GolemMaterialConfig build() {
		return ans;
	}

}
