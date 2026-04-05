package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;

@SerialClass
public class GolemCDToClient extends SerialPacketBase {

	@SerialClass.SerialField
	public int id;
	@SerialClass.SerialField
	public ArrayList<Entry> data = new ArrayList<>();

	public static void send(LivingEntity entity, MagicData data) {
		var packet = new GolemCDToClient();
		packet.id = entity.getId();
		for (var e : data.getPlayerCooldowns().getSpellCooldowns().entrySet()) {
			packet.data.add(new Entry(e.getKey(), e.getValue().getCooldownRemaining()));
		}
		GolemMagicka.HANDLER.toTrackingPlayers(packet, entity);
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		SpellInfoUpdateHandler.handle(this);
	}

	public record Entry(String id, int cd) {
	}

}
