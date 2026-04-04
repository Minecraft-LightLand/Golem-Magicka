package dev.xkmc.golemmagicka.content.entity;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.spells.fire.BurningDashSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/// Adapted from [io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob]
///
/// Should not contain any custom logic
public class GolemMagicData {

	public final AbstractGolemEntity<?, ?> golem;

	private final MagicData data = new MagicData(true);

	@Nullable
	private SpellData castingSpell;
	private CastingSpellData castingData;
	private boolean recreateSpell;
	private boolean cancelCastAnimation = false;
	private AbstractSpell lastCastSpellType = SpellRegistry.none();
	private AbstractSpell instantCastSpellType = SpellRegistry.none();

	public GolemMagicData(AbstractGolemEntity<?, ?> golem) {
		this.golem = golem;
	}

	public void onEntityEvent() {
		if (golem.level().isClientSide)
			cancelCast();
	}

	public void aiStep() {
		if (!golem.level().isClientSide()) {
			if (recreateSpell) {
				recreateSpell = false;
				SyncedSpellData syncedSpellData = data.getSyncedData();
				AbstractSpell spell = SpellRegistry.getSpell(syncedSpellData.getCastingSpellId());
				initiateCastSpell(spell, syncedSpellData.getCastingSpellLevel());
			}
			if (castingSpell != null) {
				data.handleCastDuration();
				if (data.isCasting()) {
					castingSpell.getSpell().onServerCastTick(golem.level(), castingSpell.getLevel(), golem, data);
				}

				forceLookAtTarget(golem.getTarget());
				if (data.getCastDurationRemaining() <= 0) {
					if (castingSpell.getSpell().getCastType() == CastType.LONG || castingSpell.getSpell().getCastType() == CastType.INSTANT) {
						castingSpell.getSpell().onCast(golem.level(), castingSpell.getLevel(), golem, CastSource.MOB, data);
						if (castingData != null) {
							data.addMana(-castingData.manaCost());
							GolemSpellInfoToClient.send(golem, data.getMana());
						}
					}

					castComplete();
				} else if (castingSpell.getSpell().getCastType() == CastType.CONTINUOUS && (data.getCastDurationRemaining() + 1) % 10 == 0) {
					castingSpell.getSpell().onCast(golem.level(), castingSpell.getLevel(), golem, CastSource.MOB, data);
					if (castingData != null) {
						data.addMana(-castingData.manaCost());
						GolemSpellInfoToClient.send(golem, data.getMana());
					}
				}

			}
		}

		GolemSpellManager.tickGolemSpellData(golem, data);
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		data.getSyncedData().saveNBTData(tag, golem.level().registryAccess());
		tag.putFloat("Mana", data.getMana());
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		SyncedSpellData syncedSpellData = new SyncedSpellData(golem);
		syncedSpellData.loadNBTData(tag, golem.level().registryAccess());
		if (syncedSpellData.isCasting()) {
			recreateSpell = true;
		}
		data.setSyncedData(syncedSpellData);
		if (!tag.contains("Mana")) {
			data.setMana((float) golem.getAttributeValue(AttributeRegistry.MAX_MANA.get()));
		} else {
			data.setMana(tag.getFloat("Mana"));
		}
	}

	public MagicData getMagicData() {
		return data;
	}

	public void cancelCast() {
		if (isCasting()) {
			if (golem.level().isClientSide()) {
				cancelCastAnimation = true;
			} else {
				golem.level().broadcastEntityEvent(golem, EntityEvent.STOP_OFFER_FLOWER);
			}
			castComplete();
		}

	}

	public void castComplete() {
		if (!golem.level().isClientSide) {
			if (castingSpell != null) {
				castingSpell.getSpell().onServerCastComplete(golem.level(), castingSpell.getLevel(), golem, data, false);
				if (castingData != null) {
					data.getPlayerCooldowns().addCooldown(castingData.spell().getSpellId(), castingData.cooldown());
					GolemSpellInfoToClient.send(golem, data.getMana(), castingData.spell(), castingData.cooldown());
				}

			}
		}
		data.resetCastingState();
		castingSpell = null;
		castingData = null;
	}

	public void setSyncedSpellData(SyncedSpellData syncedSpellData) {
		if (!golem.level().isClientSide()) return;
		boolean isCasting = data.isCasting();
		data.setSyncedData(syncedSpellData);
		castingSpell = data.getCastingSpell();
		if (castingSpell != null) {
			if (!data.isCasting() && isCasting) {
				castComplete();
			} else if (data.isCasting() && !isCasting) {
				AbstractSpell spell = data.getCastingSpell().getSpell();
				initiateCastSpell(spell, data.getCastingSpellLevel());
				if (castingSpell.getSpell().getCastType() == CastType.INSTANT) {
					instantCastSpellType = castingSpell.getSpell();
					castingSpell.getSpell().onClientPreCast(golem.level(), castingSpell.getLevel(), golem, InteractionHand.MAIN_HAND, data);
					castComplete();
				}
			}


		}
	}

	public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
		if (spell == SpellRegistry.none()) {
			castingSpell = null;
		} else {
			if (golem.level().isClientSide) {
				cancelCastAnimation = false;
			}

			castingSpell = new SpellData(spell, spellLevel);
			if (golem.getTarget() != null) {
				forceLookAtTarget(golem.getTarget());
			}

			if (!golem.level().isClientSide && !castingSpell.getSpell().checkPreCastConditions(golem.level(), spellLevel, golem, data)) {
				castingSpell = null;
			} else {
				if (spell != SpellRegistry.TELEPORT_SPELL.get() && spell != SpellRegistry.FROST_STEP_SPELL.get()) {
					if (spell == SpellRegistry.BLOOD_STEP_SPELL.get()) {
						setTeleportLocationBehindTarget(3);
					} else if (spell == SpellRegistry.BURNING_DASH_SPELL.get()) {
						setBurningDashDirectionData();
					}
				} else {
					setTeleportLocationBehindTarget(10);
				}

				data.initiateCast(castingSpell.getSpell(), castingSpell.getLevel(), castingSpell.getSpell().getEffectiveCastTime(castingSpell.getLevel(), golem), CastSource.MOB, SpellSelectionManager.MAINHAND);
				if (!golem.level().isClientSide) {
					castingSpell.getSpell().onServerPreCast(golem.level(), castingSpell.getLevel(), golem, data);
				}

			}
		}
	}

	public void notifyDangerousProjectile(Projectile projectile) {
	}

	public boolean isCasting() {
		return data.isCasting();
	}

	public boolean setTeleportLocationBehindTarget(int distance) {
		LivingEntity target = golem.getTarget();
		boolean valid = false;
		if (target != null) {
			Vec3 rotation = target.getLookAngle().normalize().scale(-distance);
			Vec3 pos = target.position();
			Vec3 teleportPos = rotation.add(pos);

			for (int i = 0; i < 24; ++i) {
				Vec3 randomness = Utils.getRandomVec3(0.15F * i).multiply(1.0F, 0.0F, 1.0F);
				Vec3 var10 = Utils.moveToRelativeGroundLevel(golem.level(), target.position().subtract((new Vec3(0.0F, 0.0F, (float) distance / (float) (i / 7 + 1))).yRot(-(target.getYRot() + (float) (i * 45)) * ((float) Math.PI / 180F))).add(randomness), 5);
				teleportPos = new Vec3(var10.x, var10.y + (double) 0.1F, var10.z);
				AABB reposBB = golem.getBoundingBox().move(teleportPos.subtract(golem.position()));
				if (!golem.level().collidesWithSuffocatingBlock(golem, reposBB.inflate(-0.05F))) {
					valid = true;
					break;
				}
			}
			if (valid) {
				data.setAdditionalCastData(new TeleportSpell.TeleportData(teleportPos));
			} else {
				data.setAdditionalCastData(new TeleportSpell.TeleportData(golem.position()));
			}
		} else {
			data.setAdditionalCastData(new TeleportSpell.TeleportData(golem.position()));
		}

		return valid;
	}

	public void setBurningDashDirectionData() {
		data.setAdditionalCastData(new BurningDashSpell.BurningDashDirectionOverrideCastData());
	}

	public boolean isDrinkingPotion() {
		return false;
	}

	public void startDrinkingPotion() {
	}

	public boolean getHasUsedSingleAttack() {
		return false;
	}

	public void setHasUsedSingleAttack(boolean b) {
	}

	private void forceLookAtTarget(@Nullable LivingEntity target) {
		if (target != null) {
			double d0 = target.getX() - golem.getX();
			double d2 = target.getZ() - golem.getZ();
			double d1 = target.getEyeY() - golem.getEyeY();
			double d3 = Math.sqrt(d0 * d0 + d2 * d2);
			float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
			float f1 = (float) (-(Mth.atan2(d1, d3) * (double) (180F / (float) Math.PI)));
			golem.setXRot(f1 % 360.0F);
			golem.setYRot(f % 360.0F);
		}
	}

	public void setCastingData(CastingSpellData data) {
	}

}
