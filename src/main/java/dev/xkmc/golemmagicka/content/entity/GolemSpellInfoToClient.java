package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.l2serial.network.SerialPacketBase;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record GolemSpellInfoToClient(
		int id, int mana, @Nullable String spell, int cooldown
) implements SerialPacketBase<GolemSpellInfoToClient> {

	public static void send(LivingEntity entity, float mana) {
		send(entity, mana, null, 0);
	}

	public static void send(LivingEntity entity, float mana, @Nullable AbstractSpell spell, int cd) {
		var packet = new GolemSpellInfoToClient(entity.getId(), (int) mana,
				spell == null ? null : spell.getSpellId(), cd);
		GolemMagicka.HANDLER.toTrackingPlayers(packet, entity);
	}

	public GolemSpellInfoToClient(int id, int mana) {
		this(id, mana, null, 0);
	}

	@Override
	public void handle(Player player) {
		SpellInfoUpdateHandler.handle(this);
	}

}
