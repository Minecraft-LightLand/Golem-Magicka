package dev.xkmc.golemmagicka.content.config;

import dev.xkmc.golemmagicka.content.entity.CombatMemory;
import dev.xkmc.l2core.serial.config.BaseConfig;
import dev.xkmc.l2core.serial.config.CollectType;
import dev.xkmc.l2core.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;

@SerialClass
public class SpellPriorityConfig extends BaseConfig {

	@SerialField
	@ConfigCollect(CollectType.MAP_OVERWRITE)
	public LinkedHashMap<String, Data> spellData = new LinkedHashMap<>();

	public static final Data DEF = new Data();

	public Data start(AbstractSpell spell) {
		var data = new Data();
		spellData.put(spell.getSpellId(), data);
		return data;
	}

	public Data start(String spell) {
		var data = new Data();
		spellData.put(spell, data);
		return data;
	}

	public Data get(AbstractSpell e) {
		var ans = spellData.get(e.getSpellId());
		return ans == null ? DEF : ans;
	}

	@SerialClass
	public static class Data {


		@SerialField
		public int weight = 100, minWeightCastCount, maxWeightCastCount;

		@SerialField
		public double minWeightDist, maxWeightDist, distPerLevel,
				minWeightManaFactor = 0.5, maxWeightManaFactor = 2,
				minWeightPHP, maxWeightPHP,
				aoeRange, aoeRangePerLevel, aoeCountBonus;

		@SerialField
		@Nullable
		public Holder<MobEffect> effectLock, inflictedEffect;

		public int weight(AbstractGolemEntity<?, ?> user, @Nullable LivingEntity target, MagicData magic, float totalCost, @Nullable CombatMemory memory, int level) {
			double ans = weight;
			if (effectLock != null && user.hasEffect(effectLock)) {
				return 0;
			}
			if (inflictedEffect != null && (target == null || target.hasEffect(inflictedEffect) ||
					memory != null && !memory.canInflict(inflictedEffect)))
				return 0;
			if (minWeightDist != maxWeightDist && target != null) {
				var dist = user.distanceTo(target) - distPerLevel * level;
				ans *= Mth.clamp((dist - minWeightDist) / (maxWeightDist - minWeightDist), 0, 1);
			}
			if (minWeightManaFactor != maxWeightManaFactor) {
				var mana = magic.getMana() / totalCost;
				ans *= Mth.clamp((mana - minWeightManaFactor) / (maxWeightManaFactor - minWeightManaFactor), 0, 1);
			}
			if (minWeightPHP != maxWeightPHP) {
				var php = user.getHealth() / user.getMaxHealth();
				ans *= Mth.clamp((php - minWeightPHP) / (maxWeightPHP - minWeightPHP), 0, 1);
			}
			int attackCount = memory == null ? 0 : memory.attackSpellCount();
			if (minWeightCastCount != maxWeightCastCount) {
				ans *= Mth.clamp(1d * (attackCount - minWeightCastCount) / (maxWeightCastCount - minWeightCastCount), 0, 1);
			}
			if (target != null && aoeRange > 0) {
				var range = aoeRange + aoeRangePerLevel * (level - 1);
				var pos = target.getBoundingBox().getCenter();
				if (range > maxWeightDist + distPerLevel * level && minWeightDist > maxWeightDist) {
					pos = user.getBoundingBox().getCenter();
				}
				var aabb = AABB.ofSize(pos, range * 2, range * 2, range * 2);
				int size = target.level().getEntities(EntityTypeTest.forClass(LivingEntity.class), aabb, user::predicateTarget).size();
				ans *= 1 + aoeCountBonus * size;
			}
			return (int) ans;
		}

		public float getPreferredDistSqr() {
			if (minWeightDist == maxWeightDist || maxWeightDist > 20)
				return 20 * 20;
			return (float) (maxWeightDist * maxWeightDist);
		}

		public Data weight(int weight) {
			this.weight = weight;
			return this;
		}

		public Data dist(double min, double max) {
			minWeightDist = min;
			maxWeightDist = max;
			return this;
		}

		public Data dist(double min, double max, double perLevel) {
			minWeightDist = min;
			maxWeightDist = max;
			distPerLevel = perLevel;
			return this;
		}

		public Data mana(double min, double max) {
			minWeightManaFactor = min;
			maxWeightManaFactor = max;
			return this;
		}

		public Data health(double min, double max) {
			minWeightPHP = min;
			maxWeightPHP = max;
			return this;
		}

		public Data castCount(int min, int max) {
			minWeightCastCount = min;
			maxWeightCastCount = max;
			return this;
		}

		public Data aoe(double range, double perLevel, double bonus) {
			aoeRange = range;
			aoeRangePerLevel = perLevel;
			aoeCountBonus = bonus;
			return this;
		}

		public Data effect(Holder<MobEffect> effect) {
			effectLock = effect;
			return this;
		}

		public Data targetEffect(Holder<MobEffect> effect) {
			inflictedEffect = effect;
			return this;
		}
	}

}
