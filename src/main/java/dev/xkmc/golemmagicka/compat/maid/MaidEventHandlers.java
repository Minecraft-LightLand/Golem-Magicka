package dev.xkmc.golemmagicka.compat.maid;

import dev.xkmc.golemmagicka.content.entity.IGolemMagicka;
import dev.xkmc.modulargolems.events.event.GolemInfoEvent;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;

public class MaidEventHandlers {

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onGolemInfo(GolemInfoEvent event) {
		var data = ((IGolemMagicka) event.getGolem()).magicka$getGolemMagicData().castingState;
		if (data instanceof TLMCastingAnimateStateHolder holder) {
			event.addLine(Component.literal("Spell Animation Info:"));
			event.addLine(Component.literal("- Cancelled: " + holder.isCancelled()).withStyle(ChatFormatting.GRAY));
			event.addLine(Component.literal("- Phase: " + holder.getCurrentPhase().name()).withStyle(ChatFormatting.GRAY));
			if (holder.getCastingSpellType() != SpellRegistry.none())
				event.addLine(Component.literal("- Casting Spell: " + Component.translatable(holder.getCastingSpellType().getComponentId())).withStyle(ChatFormatting.GRAY));
			if (holder.getInstantCastSpellType() != SpellRegistry.none())
				event.addLine(Component.literal("- Casted Spell: " + Component.translatable(holder.getInstantCastSpellType().getComponentId())).withStyle(ChatFormatting.GRAY));
		}
	}

}
