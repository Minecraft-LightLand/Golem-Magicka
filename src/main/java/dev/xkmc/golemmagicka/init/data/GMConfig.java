package dev.xkmc.golemmagicka.init.data;

import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.l2core.util.ConfigInit;
import net.neoforged.neoforge.common.ModConfigSpec;

public class GMConfig {

	public static class Client extends ConfigInit {

		Client(Builder builder) {
			markL2();
		}

	}

	public static class Common extends ConfigInit {

		public final ModConfigSpec.DoubleValue manaMendingRate;

		Common(Builder builder) {
			markL2();
			manaMendingRate = builder.text("Mana Mending: Mana - HP conversion rate")
					.defineInRange("manaMendingRate", 10, 0.01, 100);
		}

	}

	public static final Client CLIENT = GolemMagicka.REGISTRATE.registerClient(Client::new);

	public static final Common COMMON = GolemMagicka.REGISTRATE.registerSynced(Common::new);


	public static void init() {
	}


}
