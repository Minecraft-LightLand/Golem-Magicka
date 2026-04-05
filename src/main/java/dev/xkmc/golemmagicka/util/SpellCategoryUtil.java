package dev.xkmc.golemmagicka.util;

import dev.xkmc.golemmagicka.compat.CuriosCompat;
import dev.xkmc.golemmagicka.content.entity.SpellEntry;
import dev.xkmc.golemmagicka.init.data.GMTagGen;
import dev.xkmc.modulargolems.content.entity.common.SweepGolemEntity;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

public class SpellCategoryUtil {

	public static boolean isSupport(AbstractSpell e) {
		var holder = SpellRegistry.REGISTRY.get().getHolder(e);
		return holder.isPresent() && holder.get().is(GMTagGen.SUPPORT);
	}

	public static boolean isMovement(AbstractSpell e) {
		var holder = SpellRegistry.REGISTRY.get().getHolder(e);
		return holder.isPresent() && holder.get().is(GMTagGen.MOVEMENT);
	}

	public static boolean isDefense(AbstractSpell e) {
		var holder = SpellRegistry.REGISTRY.get().getHolder(e);
		return holder.isPresent() && holder.get().is(GMTagGen.DEFENSE);
	}

	public static boolean isSummon(AbstractSpell e) {
		var holder = SpellRegistry.REGISTRY.get().getHolder(e);
		return holder.isPresent() && holder.get().is(GMTagGen.SUMMON);
	}

	public static boolean isBanned(AbstractSpell e) {
		var holder = SpellRegistry.REGISTRY.get().getHolder(e);
		if (holder.isPresent() && holder.get().is(GMTagGen.WHITELIST)) return false;
		if (e.getRecastCount(1, null) > 0) return true;
		return holder.isEmpty() || holder.get().is(GMTagGen.BLACKLIST);
	}

	public static List<ItemStack> getGolemSpellItems(LivingEntity e) {
		List<ItemStack> list = new ArrayList<>();
		for (var s : EquipmentSlot.values()) {
			list.add(e.getItemBySlot(s));
		}
		if (e instanceof SweepGolemEntity<?, ?> s) {
			list.add(s.getBackupHand().getItem());
			list.add(s.getArrowSlot().getItem());
		}
		if (ModList.get().isLoaded(CuriosApi.MODID)) {
			CuriosCompat.getSpells(e, list);
		}
		return list;
	}

	public static List<SpellEntry> getSpells(LivingEntity e) {
		var list = getGolemSpellItems(e);
		List<SpellEntry> ans = new ArrayList<>();
		for (var stack : list) {
			if (!(stack.getItem() instanceof MagicSwordItem)) {
				ISpellContainer cont = ISpellContainer.get(stack);
				if (cont == null) continue;
				for (var spell : cont.getActiveSpells()) {
					if (SpellCategoryUtil.isBanned(spell.getSpell())) continue;
					ans.add(new SpellEntry(spell.getSpell(), spell.getLevel(), CastSource.SPELLBOOK));
				}
			}
		}
		ItemStack mainhand = e.getMainHandItem();
		if (mainhand.getItem() instanceof MagicSwordItem sword) {
			ISpellContainer cont = ISpellContainer.get(mainhand);
			if (cont == null) {
				for (var spell : sword.getSpells()) {
					if (spell == null) continue;
					if (SpellCategoryUtil.isBanned(spell.getSpell())) continue;
					ans.add(new SpellEntry(spell.getSpell(), spell.getLevel(), CastSource.SWORD));
				}
			} else {
				for (var spell : cont.getActiveSpells()) {
					if (SpellCategoryUtil.isBanned(spell.getSpell())) continue;
					ans.add(new SpellEntry(spell.getSpell(), spell.getLevel(), CastSource.SWORD));
				}
			}
		}
		return ans;
	}

	public static List<AbstractSpell> getBannedSpells(LivingEntity e) {
		var list = getGolemSpellItems(e);
		List<AbstractSpell> ans = new ArrayList<>();
		for (var stack : list) {
			if (stack.getItem() instanceof SpellBook) {
				ISpellContainer cont = ISpellContainer.get(stack);
				for (var spell : cont.getActiveSpells()) {
					if (SpellCategoryUtil.isBanned(spell.getSpell()))
						ans.add(spell.getSpell());
				}
			}
		}
		ItemStack mainhand = e.getMainHandItem();
		if (mainhand.getItem() instanceof MagicSwordItem sword) {
			for (var spell : sword.getSpells()) {
				if (spell == null) continue;
				if (SpellCategoryUtil.isBanned(spell.getSpell()))
					ans.add(spell.getSpell());
			}
		}
		return ans;
	}


}
