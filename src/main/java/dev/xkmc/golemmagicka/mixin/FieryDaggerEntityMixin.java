package dev.xkmc.golemmagicka.mixin;

import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireField;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FieryDaggerEntity.class)
public abstract class FieryDaggerEntityMixin {
    @Inject(
            method = "createFireField",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void forceCorrectOwner(CallbackInfo ci, FireField fireField) {
        FieryDaggerEntity dagger = (FieryDaggerEntity) (Object) this;
        Entity realOwner = dagger.getOwner();

        if (realOwner instanceof LivingEntity livingOwner) {
            fireField.setOwner(livingOwner);
        }
    }
}