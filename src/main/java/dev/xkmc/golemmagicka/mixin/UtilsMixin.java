package dev.xkmc.golemmagicka.mixin;

import dev.xkmc.modulargolems.content.item.equipments.MetalGolemArmorItem;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Utils.class)
public class UtilsMixin {

	@Inject(method = "canBeUpgraded", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private static void golemMagicka$golemArmor(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() instanceof MetalGolemArmorItem) {
			cir.setReturnValue(true);
		}
	}

}
