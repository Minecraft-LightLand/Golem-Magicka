package dev.xkmc.golemmagicka.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.modulargolems.content.entity.common.GuardedEntity;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FieryDaggerEntity.class)
public abstract class FieryDaggerEntityMixin {

	@WrapOperation(
			method = "createFireField",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;getNearestEntity(Ljava/lang/Class;Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;Lnet/minecraft/world/entity/LivingEntity;DDDLnet/minecraft/world/phys/AABB;)Lnet/minecraft/world/entity/LivingEntity;"
			)
	)
	private LivingEntity golemmagicka$redirectOwner(Level instance, Class aClass, TargetingConditions targetingConditions, LivingEntity livingEntity, double x, double y, double z, AABB aabb, Operation<LivingEntity> original) {
		FieryDaggerEntity dagger = (FieryDaggerEntity) (Object) this;
		if (dagger.getOwner() instanceof GuardedEntity owner) {
			return owner;
		}
		return livingEntity;
	}

}