package dev.xkmc.golemmagicka.init.data;

import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.data.DataGenerator;

public class GMConfigGen extends ConfigDataProvider {

	public GMConfigGen(DataGenerator generator) {
		super(generator, "Golem Spawn Config");
	}

	public void add(ConfigDataProvider.Collector map) {

	}

}
