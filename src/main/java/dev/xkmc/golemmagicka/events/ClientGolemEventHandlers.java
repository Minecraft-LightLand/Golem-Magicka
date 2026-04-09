package dev.xkmc.golemmagicka.events;

import dev.xkmc.golemmagicka.content.entity.GolemSpellManager;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.golemmagicka.init.data.GMLang;
import dev.xkmc.golemmagicka.util.GolemManaInfo;
import dev.xkmc.golemmagicka.util.SpellCategoryUtil;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.events.event.GolemInfoEvent;
import dev.xkmc.modulargolems.init.data.MGLangData;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

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
		if (event.getEntity() instanceof AbstractGolemEntity<?, ?> golem && golem.isAddedToLevel()) {
			SpellRenderingHelper.renderSpellHelper(ClientMagicData.getSyncedSpellData(golem), golem, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
		}
	}

	@SubscribeEvent
	public static void tooltipInject(ItemTooltipEvent event) {
		if (event.getItemStack().getItem() instanceof GolemHolder) {
			var list = event.getToolTip();
			for (int i = 0; i < list.size(); i++) {
				var line = list.get(i);
				if (line.getContents() instanceof TranslatableContents tc) {
					if (tc.getKey().equals(MGLangData.HEALTH.key())) {
						var info = GolemManaInfo.get(event.getItemStack());
						long time = event.getEntity().level().getGameTime();
						list.add(i + 1, GMLang.MANA.get(info.getMana(time), info.getMaxMana()));
						return;
					}
				}
			}
		}
	}

}
