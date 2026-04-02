package dev.xkmc.golemmagicka.init.data;

import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.l2core.util.ConfigInit;

public class GMConfig {

	public static class Client extends ConfigInit {

		Client(Builder builder) {
			markL2();
		}

	}

	public static class Common extends ConfigInit {


		Common(Builder builder) {
			markL2();
		}

	}

	public static final Client CLIENT = GolemMagicka.REGISTRATE.registerClient(Client::new);

	public static final Common COMMON = GolemMagicka.REGISTRATE.registerSynced(Common::new);


	public static void init() {
	}


}
