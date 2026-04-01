package dev.xkmc.golemmagicka.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpellRegistry.class)
public class SpellRegistryMixin {

	@WrapOperation(method = "lambda$static$0", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/registries/RegistryBuilder;disableOverrides()Lnet/minecraftforge/registries/RegistryBuilder;"))
	private static RegistryBuilder golemMagicka$tags(RegistryBuilder instance, Operation<RegistryBuilder> original) {
		instance.hasTags();
		return original.call(instance);
	}

}
