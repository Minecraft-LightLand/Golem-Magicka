package dev.xkmc.golemmagicka.init;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = GolemMagicka.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GMClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
	}

}