package dev.xkmc.golemmagicka.events;

import dev.xkmc.golemmagicka.content.entity.GolemMagicData;
import dev.xkmc.golemmagicka.content.entity.SpellEntry;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

@Cancelable
public class GolemCheckSpellEvent extends Event {

	private final AbstractGolemEntity<?, ?> golem;
	@Nullable
	private final LivingEntity target;
	private final GolemMagicData data;
	private final SpellEntry entry;

	public GolemCheckSpellEvent(AbstractGolemEntity<?, ?> golem, @Nullable LivingEntity target, GolemMagicData data, SpellEntry entry) {
		this.golem = golem;
		this.target = target;
		this.data = data;
		this.entry = entry;
	}


	public AbstractGolemEntity<?, ?> getGolem() {
		return golem;
	}

	public @Nullable LivingEntity getTarget() {
		return target;
	}

	public GolemMagicData getData() {
		return data;
	}

	public SpellEntry getEntry() {
		return entry;
	}

}
