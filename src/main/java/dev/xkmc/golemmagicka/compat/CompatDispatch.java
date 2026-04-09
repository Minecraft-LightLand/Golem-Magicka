package dev.xkmc.golemmagicka.compat;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.golemmagicka.api.IGolemCastingStateHolder;
import dev.xkmc.golemmagicka.compat.maid.TLMCastingAnimateStateHolder;
import net.neoforged.fml.ModList;

public class CompatDispatch {

	public static IGolemCastingStateHolder getCastingStateHolder() {
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			return new TLMCastingAnimateStateHolder();
		}
		return new IGolemCastingStateHolder.Dummy();
	}

}
