package dev.xkmc.golemmagicka.init.data;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Objects;

public class GMTagGen {

	public static final ProviderType<RegistrateTagsProvider.IntrinsicImpl<AbstractSpell>> SPELL_TAGS =
			ProviderType.registerIntrinsicTag("tags/spell", "spell",
					SpellRegistry.SPELL_REGISTRY_KEY, (spell) ->
							ResourceKey.create(SpellRegistry.SPELL_REGISTRY_KEY,
									Objects.requireNonNull(SpellRegistry.REGISTRY.getKey(spell))));

	public static final TagKey<AbstractSpell> WHITELIST = loc("whitelist");
	public static final TagKey<AbstractSpell> BLACKLIST = loc("blacklist");
	public static final TagKey<AbstractSpell> DEFENSE = loc("defense");
	public static final TagKey<AbstractSpell> MOVEMENT = loc("movement");
	public static final TagKey<AbstractSpell> ENHANCE = loc("enhance");
	public static final TagKey<AbstractSpell> CONTROL = loc("control");
	public static final TagKey<AbstractSpell> SUPPORT = loc("support");
	public static final TagKey<AbstractSpell> SUMMON = loc("summon");
	public static final TagKey<AbstractSpell> NON_OFFENSIVE = loc("non_offensive");
	public static final TagKey<AbstractSpell> NO_TARGET = loc("no_target");

	public static void genSpellTag(RegistrateTagsProvider.IntrinsicImpl<AbstractSpell> pvd) {
		pvd.addTag(WHITELIST).add(
				SpellRegistry.FLAMING_BARRAGE_SPELL.get(), // 炽焰追踪弹幕
				SpellRegistry.ELDRITCH_BLAST_SPELL.get(), // 邪术冲击波
				SpellRegistry.RAISE_HELL_SPELL.get() // 地狱浮现
		);
		pvd.addTag(BLACKLIST).add(
				SpellRegistry.ANGEL_WINGS_SPELL.get(), // 天使之翼
				SpellRegistry.PLANAR_SIGHT_SPELL.get(), // 位面视觉
				SpellRegistry.GLUTTONY_SPELL.get(), // 暴食
				SpellRegistry.SPECTRAL_HAMMER_SPELL.get(), // 幽冥锤
				SpellRegistry.PORTAL_SPELL.get(), // 传送门
				SpellRegistry.SUMMON_ENDER_CHEST_SPELL.get(), // 召唤末影箱
				SpellRegistry.TOUCH_DIG.get(), // 点石成掘
				SpellRegistry.WOLOLO_SPELL.get(), // Wololo
				SpellRegistry.RECALL_SPELL.get(), // 回溯
				SpellRegistry.POCKET_DIMENSION_SPELL.get(), // 口袋维度
				SpellRegistry.SACRIFICE_SPELL.get(), // 献祭
				SpellRegistry.SUMMON_HORSE_SPELL.get() // 召唤骏马
		);
		pvd.addTag(DEFENSE).add(
				SpellRegistry.SHIELD_SPELL.get(), // 护盾术
				SpellRegistry.THUNDERSTORM_SPELL.get(), // 雷暴
				SpellRegistry.FLAMING_STRIKE_SPELL.get(), // 炽焰斩击
				SpellRegistry.DIVINE_SMITE_SPELL.get(), // 神圣打击
				SpellRegistry.GUST_SPELL.get(), // 呼啸之风
				SpellRegistry.FANG_WARD_SPELL.get() // 尖牙之环
		);
		pvd.addTag(MOVEMENT).add(
				SpellRegistry.FROST_STEP_SPELL.get(), // 霜步
				SpellRegistry.BURNING_DASH_SPELL.get(), // 烈焰冲锋
				SpellRegistry.ASCENSION_SPELL.get(), // 飞升
				SpellRegistry.VOLT_STRIKE_SPELL.get(), // 伏特打击
				SpellRegistry.SHADOW_SLASH.get(), // 暗影斩击
				SpellRegistry.TELEPORT_SPELL.get(), // 传送术
				SpellRegistry.BLOOD_STEP_SPELL.get() // 血步
		);
		pvd.addTag(ENHANCE).add(
				SpellRegistry.FROSTBITE_SPELL.get(), // 霜噬
				SpellRegistry.ICE_TOMB_SPELL.get(), // 冰霜之墓
				SpellRegistry.CHARGE_SPELL.get(), // 超负荷
				SpellRegistry.ECHOING_STRIKES_SPELL.get(), // 回响打击
				SpellRegistry.EVASION_SPELL.get(), // 末影闪避
				SpellRegistry.HEAL_SPELL.get(), // 治疗
				SpellRegistry.ABYSSAL_SHROUD_SPELL.get(), // 深渊庇佑
				SpellRegistry.HEARTSTOP_SPELL.get(), // 止心术
				SpellRegistry.OAKSKIN_SPELL.get(), // 橡肤
				SpellRegistry.SPIDER_ASPECT_SPELL.get(), // 蛛毒之相
				SpellRegistry.CLEANSE_SPELL.get(), // 净化
				SpellRegistry.FORTIFY_SPELL.get(), // 神圣守护
				SpellRegistry.INVISIBILITY_SPELL.get(), // 隐身术
				SpellRegistry.GREATER_HEAL_SPELL.get() // 强效治疗
		);
		pvd.addTag(CONTROL).add(
				SpellRegistry.TELEKINESIS_SPELL.get(), // 念力
				SpellRegistry.SNOWBALL_SPELL.get(), // 雪球术
				SpellRegistry.SLOW_SPELL.get(), // 迟缓
				SpellRegistry.ROOT_SPELL.get() // 纠缠根须
		);
		pvd.addTag(SUPPORT).add(
				SpellRegistry.HEALING_CIRCLE_SPELL.get(), // 治愈之环
				SpellRegistry.HASTE_SPELL.get(), // 急迫
				SpellRegistry.BLESSING_OF_LIFE_SPELL.get() // 生命祝福
		);
		pvd.addTag(SUMMON).add(
				SpellRegistry.SUMMON_POLAR_BEAR_SPELL.get(), // 召唤北极熊
				SpellRegistry.SUMMON_VEX_SPELL.get(), // 召唤恼鬼
				SpellRegistry.SUMMON_SWORDS.get(), // 召唤利剑
				SpellRegistry.RAISE_DEAD_SPELL.get() // 驱役亡灵
		);
		pvd.addTag(NO_TARGET).add(
				SpellRegistry.ICE_TOMB_SPELL.get(), // 冰霜之墓
				SpellRegistry.HEAL_SPELL.get(), // 治疗
				SpellRegistry.HEALING_CIRCLE_SPELL.get(), // 治愈之环
				SpellRegistry.BLESSING_OF_LIFE_SPELL.get(), // 生命祝福
				SpellRegistry.CLEANSE_SPELL.get(), // 净化
				SpellRegistry.GREATER_HEAL_SPELL.get() // 强效治疗
		);
		pvd.addTag(NON_OFFENSIVE).addTags(ENHANCE, CONTROL, SUPPORT);
	}

	public static TagKey<AbstractSpell> loc(String id) {
		return TagKey.create(SpellRegistry.SPELL_REGISTRY_KEY, GolemMagicka.loc(id));
	}

}
