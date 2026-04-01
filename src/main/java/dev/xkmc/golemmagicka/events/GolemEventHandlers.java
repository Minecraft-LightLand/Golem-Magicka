package dev.xkmc.golemmagicka.events;

import dev.xkmc.golemmagicka.content.entity.GolemSpellManager;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.golemmagicka.init.data.GMLang;
import dev.xkmc.modulargolems.events.event.GolemInfoEvent;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GolemMagicka.MODID)
public class GolemEventHandlers {

	@SubscribeEvent
	public static void onGolemInfo(GolemInfoEvent event) {
		var data = ((IMagicEntity) event.getGolem()).getMagicData();
		int maxMana = (int) event.getGolem().getAttributeValue(AttributeRegistry.MAX_MANA.get());
		int mana = (int) data.getMana();
		boolean hasSpell = GolemSpellManager.predicate(event.getGolem(), event.getGolem().getMainHandItem(), null).isPresent();
		if (mana == maxMana && !hasSpell) return;
		event.addLine(GMLang.MANA.get(mana, maxMana));
		var cds = data.getPlayerCooldowns();
		if (cds.getSpellCooldowns().isEmpty()) return;
		event.addLine(GMLang.CDS.get());
		for (var e : cds.getSpellCooldowns().entrySet()) {
			var spell = SpellRegistry.getSpell(e.getKey());
			event.addLine(GMLang.CD.get(Component.translatable(spell.getComponentId()),
					e.getValue().getCooldownRemaining() / 20));
		}
	}

}
