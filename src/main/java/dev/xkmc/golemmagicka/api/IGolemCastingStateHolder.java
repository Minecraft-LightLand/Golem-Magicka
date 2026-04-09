package dev.xkmc.golemmagicka.api;

import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import net.minecraft.world.entity.LivingEntity;

public interface IGolemCastingStateHolder {

	void setCancelled(boolean cancelled);

	void clearInstantCastSpellType();

	void updateState(LivingEntity maid, SyncedSpellData syncedSpellData);

	void clearLastCastSpellType();

	record Dummy() implements IGolemCastingStateHolder {

		@Override
		public void setCancelled(boolean cancelled) {

		}

		@Override
		public void clearInstantCastSpellType() {

		}

		@Override
		public void updateState(LivingEntity maid, SyncedSpellData syncedSpellData) {

		}

		@Override
		public void clearLastCastSpellType() {

		}

	}

}