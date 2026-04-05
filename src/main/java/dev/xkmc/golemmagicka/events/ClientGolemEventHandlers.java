package dev.xkmc.golemmagicka.events;

import dev.xkmc.golemmagicka.content.entity.GolemSpellManager;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.golemmagicka.init.data.GMLang;
import dev.xkmc.golemmagicka.util.SpellCategoryUtil;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.events.event.GolemInfoEvent;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = GolemMagicka.MODID)
public class ClientGolemEventHandlers {

	@SubscribeEvent
	public static void onGolemInfo(GolemInfoEvent event) {
		var data = ((IMagicEntity) event.getGolem()).getMagicData();
		int maxMana = (int) event.getGolem().getAttributeValue(AttributeRegistry.MAX_MANA);
		int mana = (int) data.getMana();
		boolean hasSpell = GolemSpellManager.predicate(event.getGolem(), event.getGolem().getMainHandItem(), null).isPresent();
		if (mana == maxMana && !hasSpell) return;
		event.addLine(GMLang.MANA.get(mana, maxMana));
		var cds = data.getPlayerCooldowns();
		if (!cds.getSpellCooldowns().isEmpty()) {
			event.addLine(GMLang.CDS.get());
			for (var e : cds.getSpellCooldowns().entrySet()) {
				var spell = SpellRegistry.getSpell(e.getKey());
				event.addLine(GMLang.CD.get(Component.translatable(spell.getComponentId()),
						e.getValue().getCooldownRemaining() / 20));
			}
		}
		var invalid = SpellCategoryUtil.getBannedSpells(event.getGolem());
		if (!invalid.isEmpty()) {
			event.addLine(GMLang.INVALID_SPELLS.get());
			for (var spell : invalid) {
				event.addLine(GMLang.INVALID_SPELL.get(Component.translatable(spell.getComponentId())));
			}
		}
	}

	@SubscribeEvent
	public static void renderGolem(RenderLivingEvent.Post<?, ?> event) {
		if (event.getEntity() instanceof AbstractGolemEntity<?, ?> golem) {
			SpellRenderingHelper.renderSpellHelper(ClientMagicData.getSyncedSpellData(golem), golem, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
		}
	}

}
