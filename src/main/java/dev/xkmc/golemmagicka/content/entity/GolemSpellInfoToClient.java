package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class GolemSpellInfoToClient extends SerialPacketBase {

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
