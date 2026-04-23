package dev.xkmc.golemmagicka.mixin;

import dev.xkmc.golemmagicka.init.data.GMTagGen;
import dev.xkmc.golemmagicka.util.SpellCategoryUtil;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemArmorItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemWeaponItem;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(Utils.class)
public class UtilsMixin {

	@Inject(method = "canBeUpgraded", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private static void golemMagicka$golemArmor(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() instanceof MetalGolemArmorItem) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "canImbue", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private static void golemMagicka$golemWeapon(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() instanceof MetalGolemWeaponItem) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "preCastTargetHelper(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lio/redspace/ironsspellbooks/api/magic/MagicData;Lio/redspace/ironsspellbooks/api/spells/AbstractSpell;IFZLjava/util/function/Predicate;)Z", at = @At("HEAD"), cancellable = true, remap = false)
	private static void golemMagicka$golemSupport(Level level, LivingEntity caster, MagicData playerMagicData, AbstractSpell spell, int range, float aimAssist, boolean sendFailureMessage, Predicate<LivingEntity> filter, CallbackInfoReturnable<Boolean> cir) {
		if (SpellCategoryUtil.is(spell, GMTagGen.SUPPORT)) {
			playerMagicData.setAdditionalCastData(new TargetEntityCastData(caster));
			cir.setReturnValue(true);
		}
	}

}
