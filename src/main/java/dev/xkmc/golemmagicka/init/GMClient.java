package dev.xkmc.golemmagicka.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GolemMagicka.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GMClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
	}

	@SubscribeEvent
	public static void registerOverlays(RegisterGuiOverlaysEvent event) {
	}

	@SubscribeEvent
	public static void registerArmorLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
	}

}