package dev.xkmc.golemmagicka.content.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.content.modifier.special.EarthquakeHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PyriumJumpModifier extends GolemModifier implements EarthquakeHelper.Modifier {

	public PyriumJumpModifier() {
		super(StatFilterType.HEALTH, 2);
	}

	@Override
	public void onRegisterFlag(Consumer<GolemFlags> addFlag) {
		addFlag.accept(GolemFlags.EARTH_QUAKE);
	}

	@Override
	public void performEarthQuake(AbstractGolemEntity<?, ?> golem, int lv) {
		var level = golem.level();
		var aabb = golem.getBoundingBox().inflate(16, 6, 16);
		List<LivingEntity> targets = new ArrayList<>();

		for (var e : level.getEntitiesOfClass(LivingEntity.class, aabb)) {
			if (!golem.predicateTarget(e)) continue;
			targets.add(e);
		}
		if (targets.isEmpty()) return;

		float atk = (float) golem.getAttributeValue(Attributes.ATTACK_DAMAGE);
		float spellPower = (float) golem.getAttributeValue(AttributeRegistry.SPELL_POWER.get()) + (float) golem.getAttributeValue(AttributeRegistry.FIRE_SPELL_POWER.get());
		float damage = atk * 0.75f * (1 + spellPower);

		golem.playSound(SoundRegistry.FIERY_DAGGER_THROW.get(), 2.0F, 1.0F);

		Vec3 start = golem.getEyePosition();

		for (int i = 0; i < 3; i++) {
			LivingEntity target = targets.get(i % targets.size());
			Vec3 targetPos = target.position();
			Vec3 deltaAim = targetPos.subtract(start);

			FieryDaggerEntity dagger = new FieryDaggerEntity(level);
			dagger.setOwner(golem);
			dagger.setPos(start);
			dagger.setDamage(damage / 3);
			dagger.setExplosionRadius(4.0F + level.random.nextFloat() * 2.0F);
			dagger.delay = 10 + level.random.nextInt(20);
			dagger.setNoGravity(false);

			Vec3 aim = start.add(deltaAim.yRot(((float) Math.PI / 4F) * (float) (i - 1)));
			Vec3 horizontal = aim.subtract(start).multiply(1.0, 0.0, 1.0);
			double horizontalSpeed = (double) (1.0F * Mth.cos(((float) Math.PI / 4F))) + 0.5;
			double distance = horizontal.length();
			double ticks = distance / horizontalSpeed;
			double y1 = aim.y - start.y;
			double g = 0.05;
			double verticalSpeed = (y1 + 0.5 * g * ticks * ticks) / ticks;
			Vec3 trajectory = horizontal.normalize().scale(horizontalSpeed).add(0, verticalSpeed, 0);
			dagger.setDeltaMovement(trajectory);
			level.addFreshEntity(dagger);
		}
	}

	@Override
	public double getEarthquakeRangeSqr(AbstractGolemEntity<?, ?> golem, LivingEntity target, int lv) {
		return 625.0;
	}

	@Override
	public void performJump(AbstractGolemEntity<?, ?> golem, int lv) {
		var target = golem.getTarget();
		if (target == null) {
			golem.addDeltaMovement(new Vec3(0, 1.3, 0));
		} else {
			golem.setDeltaMovement((target.getX() - golem.getX()) * 0.15, 1.3, (target.getZ() - golem.getZ()) * 0.15);
		}
	}

	@Override
	public int getCoolDown(AbstractGolemEntity<?, ?> golem, int lv) {
		return 200;
	}
}