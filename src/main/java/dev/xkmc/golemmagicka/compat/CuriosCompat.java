package dev.xkmc.golemmagicka.compat;

import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class CuriosCompat {

	public static void getSpells(LivingEntity e, List<ItemStack> list) {
		var opt = CuriosApi.getCuriosInventory(e);
		if (opt.isEmpty()) return;
		var generic = opt.get().findCurios(x -> x.getItem() instanceof SpellBook);
		for (var s : generic) {
			if (!s.stack().isEmpty())
				list.add(s.stack());
		}
	}

}
