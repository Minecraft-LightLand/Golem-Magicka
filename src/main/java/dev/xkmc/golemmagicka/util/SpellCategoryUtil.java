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
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

public class SpellCategoryUtil {

	public static boolean is(AbstractSpell e, TagKey<AbstractSpell> tag) {
		return SpellRegistry.REGISTRY.wrapAsHolder(e).is(tag);
	}

	public static boolean nonOffensive(AbstractSpell e) {
		return is(e, GMTagGen.NON_OFFENSIVE);
	}

	public static boolean isBanned(AbstractSpell e) {
		if (is(e, GMTagGen.WHITELIST)) return false;
		if (e.getRecastCount(1, null) > 0) return true;
		return is(e, GMTagGen.BLACKLIST);
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


	public static List<ItemStack> getGolemImbuedItems(LivingEntity e) {
		List<ItemStack> list = new ArrayList<>();
		for (var s : EquipmentSlot.values()) {
			list.add(e.getItemBySlot(s));
		}
		if (e instanceof SweepGolemEntity<?, ?> s) {
			list.add(s.getBackupHand().getItem());
		}
		return list;
	}

	public static List<SpellEntry> getSpells(LivingEntity e) {
		List<SpellEntry> ans = new ArrayList<>();
		for (var stack : getGolemSpellItems(e)) {
			if (stack.getItem() instanceof SpellBook) {
				ISpellContainer cont = ISpellContainer.get(stack);
				if (cont == null) continue;
				for (var spell : cont.getActiveSpells()) {
					if (isBanned(spell.getSpell())) continue;
					ans.add(new SpellEntry(spell.getSpell(), spell.getLevel(), CastSource.SPELLBOOK, stack));
				}
			}
		}
		for (var stack : getGolemImbuedItems(e)) {
			if (stack.getItem() instanceof SpellBook) continue;
			ISpellContainer cont = ISpellContainer.get(stack);
			if (cont != null) {
				for (var spell : cont.getActiveSpells()) {
					if (isBanned(spell.getSpell())) continue;
					ans.add(new SpellEntry(spell.getSpell(), spell.getLevel(), CastSource.SWORD, stack));
				}
				continue;
			}
			if (stack.getItem() instanceof MagicSwordItem sword) {
				for (var spell : sword.getSpells()) {
					if (spell == null) continue;
					if (isBanned(spell.getSpell())) continue;
					ans.add(new SpellEntry(spell.getSpell(), spell.getLevel(), CastSource.SWORD, stack));
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
					if (isBanned(spell.getSpell()))
						ans.add(spell.getSpell());
				}
			}
		}
		ItemStack mainhand = e.getMainHandItem();
		if (mainhand.getItem() instanceof MagicSwordItem sword) {
			for (var spell : sword.getSpells()) {
				if (spell == null) continue;
				if (isBanned(spell.getSpell()))
					ans.add(spell.getSpell());
			}
		}
		return ans;
	}


}
