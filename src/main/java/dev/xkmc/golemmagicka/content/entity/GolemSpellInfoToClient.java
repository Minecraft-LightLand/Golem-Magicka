package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class GolemSpellInfoToClient extends SerialPacketBase {

	public static void send(LivingEntity entity, float mana) {
		send(entity, mana, null, 0);
	}

	public static void send(LivingEntity entity, float mana, @Nullable AbstractSpell spell, int cd) {
		var packet = new GolemSpellInfoToClient();
		packet.id = entity.getId();
		packet.mana = (int) mana;
		packet.spell = spell == null ? null : spell.getSpellId();
		packet.cooldown = cd;
		GolemMagicka.HANDLER.toTrackingPlayers(packet, entity);
	}

	@SerialClass.SerialField
	public int id;

	@SerialClass.SerialField
	public int mana;

	@SerialClass.SerialField
	public String spell;

	@SerialClass.SerialField
	public int cooldown;

	public GolemSpellInfoToClient() {

	}

	@Override
	public void handle(NetworkEvent.Context context) {
		SpellInfoUpdateHandler.handle(this);
	}

}
