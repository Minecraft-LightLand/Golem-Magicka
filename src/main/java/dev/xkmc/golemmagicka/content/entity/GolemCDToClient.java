package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.l2serial.network.SerialPacketBase;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public record GolemCDToClient(
		int id, ArrayList<Entry> data
) implements SerialPacketBase<GolemCDToClient> {

	public static void send(LivingEntity entity, MagicData data) {
		var list = new ArrayList<Entry>();
		for (var e : data.getPlayerCooldowns().getSpellCooldowns().entrySet()) {
			list.add(new Entry(e.getKey(), e.getValue().getCooldownRemaining()));
		}
		var packet = new GolemCDToClient(entity.getId(), list);
		GolemMagicka.HANDLER.toTrackingPlayers(packet, entity);
	}

	@Override
	public void handle(Player player) {
		SpellInfoUpdateHandler.handle(this);
	}

	public record Entry(String id, int cd) {
	}

}
