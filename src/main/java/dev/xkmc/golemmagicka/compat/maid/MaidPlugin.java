package dev.xkmc.golemmagicka.compat.maid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.magic.MagicCastingAnimationManager;

@LittleMaidExtension
public class MaidPlugin implements ILittleMaid {

	@Override
	public void registerMagicCastingAnimation(MagicCastingAnimationManager manager) {
		manager.register(new GolemMaidAnimationProvider());
	}

}
