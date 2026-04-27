package dev.xkmc.golemmagicka.content.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

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
		protected boolean performAttack(LivingEntity target) {
			spawnFieryDaggerSwarm(golem, target, lv);
			return true;
		}
	}

	private static void spawnFieryDaggerSwarm(AbstractGolemEntity<?, ?> golem, LivingEntity target, int lv) {
		var level = golem.level();
		Vec3 pos = golem.position();
		int count = 7;
		int delay = Mth.nextInt(golem.getRandom(), 30, 70);
		float yAngle = (float) (-Mth.atan2(target.getZ() - golem.getZ(), target.getX() - golem.getX()) + Mth.HALF_PI);
		float scale = golem.getScale();
		float atk = (float) golem.getAttributeValue(Attributes.ATTACK_DAMAGE);
		float spellPower = (float) golem.getAttributeValue(AttributeRegistry.SPELL_POWER.get()) + (float) golem.getAttributeValue(AttributeRegistry.FIRE_SPELL_POWER.get());
		float damage = atk * 0.75f * (1 + spellPower);
		for (int i = 0; i < count; i++) {
			Vec3 offset = new Vec3(1.5 * scale, 0, 0)
					.zRot(Mth.lerp((float) i / (count - 1), 0, -Mth.PI))
					.yRot(yAngle)
					.add(0, golem.getEyeHeight(), 0);
			FieryDaggerEntity dagger = new FieryDaggerEntity(level);
			dagger.setOwner(golem);
			dagger.ownerTrack = offset;
			dagger.setTarget(target);
			dagger.setPos(pos.add(offset.yRot(golem.getYRot())));
			dagger.delay = delay + i * 2;
			dagger.setDamage(damage / 7);
			level.addFreshEntity(dagger);
		}
	}

}