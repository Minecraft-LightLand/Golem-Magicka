package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record GolemSpellInfoToClient(
		int id, int mana, @Nullable String spell, int cooldown
) implements SerialPacketBase<GolemSpellInfoToClient> {

	public GolemSpellInfoToClient(int id, int mana) {
		this(id, mana, null, 0);
	}

	@Override
	public void handle(Player player) {
		SpellInfoUpdateHandler.handle(this);
	}

}
