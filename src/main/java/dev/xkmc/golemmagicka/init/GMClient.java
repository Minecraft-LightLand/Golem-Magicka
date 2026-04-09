package dev.xkmc.golemmagicka.init;

import dev.xkmc.golemmagicka.content.client.GolemEnergySwirlLayer;
import dev.xkmc.golemmagicka.content.client.GolemModelDefinitions;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemModel;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemRenderer;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemModel;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemRenderer;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemModel;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemRenderer;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.render.EnergySwirlLayer;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = GolemMagicka.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GMClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
	}

	@SubscribeEvent
	public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(GolemEnergySwirlLayer.LARGE, () -> GolemModelDefinitions.createGolemLayer(new CubeDeformation(0.3f)));
		event.registerLayerDefinition(GolemEnergySwirlLayer.HUMANOID, () -> GolemModelDefinitions.createHumanoidLayer(new CubeDeformation(0.3f)));
		event.registerLayerDefinition(GolemEnergySwirlLayer.DOG, () -> GolemModelDefinitions.createDogLayer(new CubeDeformation(0.3f)));
	}

	@SubscribeEvent
	public static void addLayer(EntityRenderersEvent.AddLayers event) {
		var set = event.getEntityModels();
		if (event.getRenderer(GolemTypes.ENTITY_GOLEM.get()) instanceof MetalGolemRenderer ler) {
			ler.addLayer(new GolemEnergySwirlLayer<>(ler, new MetalGolemModel(set.bakeLayer(GolemEnergySwirlLayer.LARGE)), EnergySwirlLayer.EVASION_TEXTURE, MobEffectRegistry.EVASION));
			ler.addLayer(new GolemEnergySwirlLayer<>(ler, new MetalGolemModel(set.bakeLayer(GolemEnergySwirlLayer.LARGE)), EnergySwirlLayer.CHARGE_TEXTURE, MobEffectRegistry.CHARGED));
		}
		if (event.getRenderer(GolemTypes.ENTITY_HUMANOID.get()) instanceof HumanoidGolemRenderer ler) {
			ler.addLayer(new GolemEnergySwirlLayer<>(ler, new HumanoidGolemModel(set.bakeLayer(GolemEnergySwirlLayer.HUMANOID),false), EnergySwirlLayer.EVASION_TEXTURE, MobEffectRegistry.EVASION));
			ler.addLayer(new GolemEnergySwirlLayer<>(ler, new HumanoidGolemModel(set.bakeLayer(GolemEnergySwirlLayer.HUMANOID),false), EnergySwirlLayer.CHARGE_TEXTURE, MobEffectRegistry.CHARGED));
		}
		if (event.getRenderer(GolemTypes.ENTITY_DOG.get()) instanceof DogGolemRenderer ler) {
			ler.addLayer(new GolemEnergySwirlLayer<>(ler, new DogGolemModel(set.bakeLayer(GolemEnergySwirlLayer.DOG)), EnergySwirlLayer.EVASION_TEXTURE, MobEffectRegistry.EVASION));
			ler.addLayer(new GolemEnergySwirlLayer<>(ler, new DogGolemModel(set.bakeLayer(GolemEnergySwirlLayer.DOG)), EnergySwirlLayer.CHARGE_TEXTURE, MobEffectRegistry.CHARGED));
		}
	}

}