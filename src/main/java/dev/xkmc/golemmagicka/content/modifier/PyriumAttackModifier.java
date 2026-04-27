package dev.xkmc.golemmagicka.content.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.function.BiConsumer;

public class PyriumAttackModifier extends GolemModifier {

	public PyriumAttackModifier() {
		super(StatFilterType.ATTACK, 2);
	}

	@Override
	public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
		addGoal.accept(2, new AttackGoal(entity, lv));
	}

	public static class AttackGoal extends BaseRangedAttackGoal {

		public AttackGoal(AbstractGolemEntity<?, ?> golem, int lv) {
			super(100, 0, 35, golem, lv);
		}

		@Override
		protected boolean performAttack(LivingEntity livingEntity) {
			//TODO
			return true;
		}

	}

}
