package dev.xkmc.golemmagicka.mixin;

import dev.xkmc.golemmagicka.content.entity.GolemMagicData;
import dev.xkmc.golemmagicka.content.entity.IGolemMagicka;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GuardedEntity;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractGolemEntity.class)
public abstract class AbstractGolemMixin extends GuardedEntity implements IMagicEntity, IGolemMagicka {

	@Unique
	private final GolemMagicData magicka$magicData = new GolemMagicData(Wrappers.cast(this));

	protected AbstractGolemMixin(EntityType<? extends AbstractGolem> type, Level level) {
		super(type, level);
	}

	@Inject(method = "handleEntityEvent", at = @At("HEAD"), cancellable = true)
	public void magicka$handleEvent(byte event, CallbackInfo ci) {
		if (magicka$magicData == null) return;
		if (event == EntityEvent.STOP_OFFER_FLOWER) {
			magicka$magicData.onEntityEvent();
			ci.cancel();
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void magicka$readAdditionalData(CompoundTag tag, CallbackInfo ci) {
		if (magicka$magicData == null) return;
		magicka$magicData.readAdditionalSaveData(tag);
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void magicka$writeAdditionalData(CompoundTag tag, CallbackInfo ci) {
		if (magicka$magicData == null) return;
		magicka$magicData.addAdditionalSaveData(tag);
	}

	@Inject(method = "aiStep", at = @At("TAIL"))
	public void magicka$aiStep(CallbackInfo ci) {
		if (magicka$magicData == null) return;
		if (this.isAlive())
			magicka$magicData.aiStep();
	}

	@Override
	public @NotNull GolemMagicData magicka$getGolemMagicData() {
		return magicka$magicData;
	}

	@Override
	public MagicData getMagicData() {
		return magicka$magicData.getMagicData();
	}

	@Override
	public void setSyncedSpellData(SyncedSpellData syncedSpellData) {
		magicka$magicData.setSyncedSpellData(syncedSpellData);
	}

	@Override
	public boolean isCasting() {
		return magicka$magicData.isCasting();
	}

	@Override
	public void initiateCastSpell(AbstractSpell abstractSpell, int i) {
		magicka$magicData.initiateCastSpell(abstractSpell, i);
	}

	@Override
	public void cancelCast() {
		magicka$magicData.cancelCast();
	}

	@Override
	public void castComplete() {
		magicka$magicData.castComplete();
	}

	@Override
	public void notifyDangerousProjectile(Projectile projectile) {
		magicka$magicData.notifyDangerousProjectile(projectile);
	}

	@Override
	public boolean setTeleportLocationBehindTarget(int i) {
		return magicka$magicData.setTeleportLocationBehindTarget(i);
	}

	@Override
	public void setBurningDashDirectionData() {
		magicka$magicData.setBurningDashDirectionData();
	}

	@Override
	public boolean isDrinkingPotion() {
		return magicka$magicData.isDrinkingPotion();
	}

	@Override
	public boolean getHasUsedSingleAttack() {
		return magicka$magicData.getHasUsedSingleAttack();
	}

	@Override
	public void setHasUsedSingleAttack(boolean b) {
		magicka$magicData.setHasUsedSingleAttack(b);
	}

	@Override
	public void startDrinkingPotion() {
		magicka$magicData.startDrinkingPotion();
	}

}
