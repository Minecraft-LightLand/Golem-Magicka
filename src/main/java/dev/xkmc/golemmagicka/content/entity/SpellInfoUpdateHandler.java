package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.client.Minecraft;

public class SpellInfoUpdateHandler {

	public static void handle(GolemSpellInfoToClient packet) {
		var level = Minecraft.getInstance().level;
		if (level == null) return;
		var e = level.getEntity(packet.id());
		if (!(e instanceof AbstractGolemEntity<?, ?> golem)) return;
		var data = ((IMagicEntity) golem).getMagicData();
		data.setMana(packet.mana());
		if (packet.spell() == null) return;
		var spell = SpellRegistry.getSpell(packet.spell());
		if (spell == null) return;
		if (packet.cooldown() == 0) return;
		data.getPlayerCooldowns().addCooldown(packet.spell(), packet.cooldown());
	}

	public static void handle(GolemCDToClient packet) {
		var level = Minecraft.getInstance().level;
		if (level == null) return;
		var e = level.getEntity(packet.id());
		if (!(e instanceof AbstractGolemEntity<?, ?> golem)) return;
		var data = ((IMagicEntity) golem).getMagicData();
		for (var pair : packet.data()) {
			data.getPlayerCooldowns().addCooldown(pair.id(), pair.cd());
		}
	}

}
