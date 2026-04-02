package dev.xkmc.golemmagicka.events;

import dev.xkmc.golemmagicka.content.entity.GolemSpellManager;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.events.event.GolemEquipItemEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = GolemMagicka.MODID)
public class GolemEventHandlers {

	@SubscribeEvent
	public static void onGolemItem(GolemEquipItemEvent event) {
		if (event.getEntity() instanceof MetalGolemEntity e) {
			if (GolemSpellManager.predicate(e, event.getStack(), null).isPresent()) {
				event.setSlot(1, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
			}
		}
	}

}
