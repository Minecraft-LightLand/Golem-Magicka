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
			ProviderType.register("tags/spell", (type) -> (p, e) ->
					new RegistrateTagsProvider.IntrinsicImpl<>(p, type, "spells", e.getGenerator().getPackOutput(),
							SpellRegistry.SPELL_REGISTRY_KEY, e.getLookupProvider(), (spell) ->
							ResourceKey.create(SpellRegistry.SPELL_REGISTRY_KEY,
									Objects.requireNonNull(SpellRegistry.REGISTRY.get().getKey(spell))),
							e.getExistingFileHelper()));

	public static final TagKey<AbstractSpell> WHITELIST = loc("whitelist");
	public static final TagKey<AbstractSpell> BLACKLIST = loc("blacklist");
	public static final TagKey<AbstractSpell> MELEE_SPELL = loc("melee_spell");
	public static final TagKey<AbstractSpell> MELEE_ATTACK_SPELL = loc("melee_attack_spell");
	public static final TagKey<AbstractSpell> DEFENSE = loc("defense");
	public static final TagKey<AbstractSpell> MOVEMENT = loc("movement");
	public static final TagKey<AbstractSpell> ENHANCE = loc("enhance");
	public static final TagKey<AbstractSpell> CONTROL = loc("control");
	public static final TagKey<AbstractSpell> SUPPORT = loc("support");
	public static final TagKey<AbstractSpell> SUMMON = loc("summon");
	public static final TagKey<AbstractSpell> NON_OFFENSIVE = loc("non_offensive");
	public static final TagKey<AbstractSpell> NO_TARGET = loc("no_target");

	public static void genSpellTag(RegistrateTagsProvider.IntrinsicImpl<AbstractSpell> pvd) {
		pvd.addTag(WHITELIST).add( // 法术白名单
				SpellRegistry.FLAMING_BARRAGE_SPELL.get(), // 炽焰追踪弹幕
				SpellRegistry.ELDRITCH_BLAST_SPELL.get(), // 邪术冲击波
				SpellRegistry.RAISE_HELL_SPELL.get() // 地狱浮现
		);
		pvd.addTag(BLACKLIST).add( // 法术黑名单
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
		pvd.addTag(MELEE_SPELL).addTags(MELEE_ATTACK_SPELL, DEFENSE, MOVEMENT, ENHANCE, CONTROL, SUPPORT).add( // 近战可释放法术
				SpellRegistry.THUNDERSTORM_SPELL.get(), // 雷暴
				SpellRegistry.GUST_SPELL.get() // 呼啸之风
		);
		pvd.addTag(MELEE_ATTACK_SPELL).add( // 近战攻击法术
				SpellRegistry.CONE_OF_COLD_SPELL.get(), // 刺骨寒风
				SpellRegistry.FIRE_BREATH_SPELL.get(), // 火焰吐息
				SpellRegistry.ELECTROCUTE_SPELL.get(), // 电刑
				SpellRegistry.DRAGON_BREATH_SPELL.get(), // 龙息
				SpellRegistry.POISON_BREATH_SPELL.get(), // 毒雾喷射
				SpellRegistry.RAISE_HELL_SPELL.get(), // 地狱浮现
				SpellRegistry.FLAMING_STRIKE_SPELL.get(), // 炽焰斩击
				SpellRegistry.DIVINE_SMITE_SPELL.get(), // 神圣打击
				SpellRegistry.STOMP_SPELL.get(), // 践踏
				SpellRegistry.FANG_WARD_SPELL.get(), // 尖牙之环
				SpellRegistry.SHOCKWAVE_SPELL.get() // 震荡波
		);
		pvd.addTag(DEFENSE).add( // 防御法术
				SpellRegistry.SHIELD_SPELL.get(), // 护盾术
				SpellRegistry.ICE_TOMB_SPELL.get(), // 冰霜之墓
				SpellRegistry.EVASION_SPELL.get(), // 末影闪避
				SpellRegistry.ABYSSAL_SHROUD_SPELL.get(), // 深渊庇佑
				SpellRegistry.HEARTSTOP_SPELL.get(), // 止心术
				SpellRegistry.INVISIBILITY_SPELL.get() // 隐身术
		);
		pvd.addTag(MOVEMENT).add( // 可移动法术
				SpellRegistry.FROST_STEP_SPELL.get(), // 霜步
				SpellRegistry.BURNING_DASH_SPELL.get(), // 烈焰冲锋
				SpellRegistry.ASCENSION_SPELL.get(), // 飞升
				SpellRegistry.VOLT_STRIKE_SPELL.get(), // 伏特打击
				SpellRegistry.SHADOW_SLASH.get(), // 暗影斩击
				SpellRegistry.TELEPORT_SPELL.get(), // 传送术
				SpellRegistry.BLOOD_STEP_SPELL.get() // 血步
		);
		pvd.addTag(ENHANCE).add( // 增益法术
				SpellRegistry.FROSTBITE_SPELL.get(), // 霜噬
				SpellRegistry.CHARGE_SPELL.get(), // 超负荷
				SpellRegistry.ECHOING_STRIKES_SPELL.get(), // 回响打击
				SpellRegistry.HEAL_SPELL.get(), // 治疗
				SpellRegistry.OAKSKIN_SPELL.get(), // 橡肤
				SpellRegistry.SPIDER_ASPECT_SPELL.get(), // 蛛毒之相
				SpellRegistry.CLEANSE_SPELL.get(), // 净化
				SpellRegistry.FORTIFY_SPELL.get(), // 神圣守护
				SpellRegistry.GREATER_HEAL_SPELL.get() // 强效治疗
		);
		pvd.addTag(CONTROL).add( // 控制法术
				SpellRegistry.TELEKINESIS_SPELL.get(), // 念力
				SpellRegistry.SNOWBALL_SPELL.get(), // 雪球术
				SpellRegistry.SLOW_SPELL.get(), // 迟缓
				SpellRegistry.ROOT_SPELL.get() // 纠缠根须
		);
		pvd.addTag(SUPPORT).add( // 指向增益法术
				SpellRegistry.HEALING_CIRCLE_SPELL.get(), // 治愈之环
				SpellRegistry.HASTE_SPELL.get(), // 急迫
				SpellRegistry.BLESSING_OF_LIFE_SPELL.get() // 生命祝福
		);
		pvd.addTag(SUMMON).add( // 召唤法术
				SpellRegistry.SUMMON_POLAR_BEAR_SPELL.get(), // 召唤北极熊
				SpellRegistry.SUMMON_VEX_SPELL.get(), // 召唤恼鬼
				SpellRegistry.SUMMON_SWORDS.get(), // 召唤利剑
				SpellRegistry.RAISE_DEAD_SPELL.get() // 驱役亡灵
		);
		pvd.addTag(NO_TARGET).add( // 空闲释放法术
				SpellRegistry.ICE_TOMB_SPELL.get(), // 冰霜之墓
				SpellRegistry.HEAL_SPELL.get(), // 治疗
				SpellRegistry.HEALING_CIRCLE_SPELL.get(), // 治愈之环
				SpellRegistry.BLESSING_OF_LIFE_SPELL.get(), // 生命祝福
				SpellRegistry.CLEANSE_SPELL.get(), // 净化
				SpellRegistry.GREATER_HEAL_SPELL.get() // 强效治疗
		);
		pvd.addTag(NON_OFFENSIVE).addTags(ENHANCE, CONTROL, SUPPORT); // 不增加使用次数法术
	}

	public static TagKey<AbstractSpell> loc(String id) {
		return TagKey.create(SpellRegistry.SPELL_REGISTRY_KEY, GolemMagicka.loc(id));
	}

}
