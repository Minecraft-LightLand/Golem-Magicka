package dev.xkmc.golemmagicka.init.data;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.golemmagicka.content.config.MagicStatBuilder;
import dev.xkmc.golemmagicka.content.config.SpellPriorityConfig;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.golemmagicka.init.reg.GMTypes;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.init.data.AttrDispEntry;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.config.GolemPartConfig;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class GMConfigGen extends ConfigDataProvider {

	public GMConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "Golem Spawn Config");
	}

	public void add(ConfigDataProvider.Collector map) {

		map.add(ModularGolems.PARTS, GolemMagicka.loc("magic"), new GolemPartConfig()
				.addEntity(GolemTypes.TYPE_GOLEM.get())
				.addFilter(GMTypes.STAT_MAX_MANA.get(), 2)
				.end()

				.addEntity(GolemTypes.TYPE_DOG.get())
				.addFilter(GMTypes.STAT_MAX_MANA.get(), 0.5)
				.addFilter(GMTypes.STAT_CD.get(), 0)
				.addFilter(GMTypes.STAT_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.FIRE_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.ICE_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.LIGHTNING_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.HOLY_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.ENDER_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.BLOOD_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.EVOCATION_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.NATURE_SPELL_POWER.get(), 0)
				.addFilter(GMTypes.ELDRITCH_SPELL_POWER.get(), 0)
				.end()

		);

		map.add(ModularGolems.MATERIALS, GolemMagicka.loc("vanilla"), new MagicStatBuilder()
				.add(ModularGolems.loc("gold"), 100, 0.4)
				.add(ModularGolems.loc("netherite"), 200, 0.4)
				.add(ModularGolems.loc("sculk"), 100, 0.2)
				.add(ModularGolems.loc("sculk"), GMTypes.ELDRITCH_SPELL_POWER.get(), 0.4)
				.add(ModularGolems.loc("copper"), GMTypes.LIGHTNING_RESIST.get(), 0.5)
				.add(ModularGolems.loc("netherite"), GMTypes.FIRE_RESIST.get(), 0.8)
				.build()
		);

		map.add(ModularGolems.MATERIALS, GolemMagicka.loc("iron_spells"), new GolemMaterialConfig()
				.addMaterial(GolemMagicka.loc("pyrium"), Ingredient.of(ItemRegistry.PYRIUM_INGOT.get()))
				.addStat(GolemTypes.STAT_HEALTH.get(), 400)
				.addStat(GolemTypes.STAT_ATTACK.get(), 30)
				.addStat(GolemTypes.STAT_SWEEP.get(), 1)
				.addStat(GMTypes.STAT_MAX_MANA.get(), 800)
				.addStat(GMTypes.STAT_MANA_REGEN.get(), 1)
				.addStat(GMTypes.STAT_SPELL_POWER.get(), 0.5)
				.addStat(GMTypes.STAT_CD.get(), 0.4)
				.addStat(GMTypes.FIRE_RESIST.get(), 1)
				.addModifier(GolemModifiers.DAMAGE_CAP.get(), 1)
				.addModifier(GolemModifiers.FIRE_IMMUNE.get(), 1)
				.end()
		);

		genSpell(map);

	}

	public static void genDataMap(RegistrateDataMapProvider pvd) {
		pvd.builder(L2Tabs.ATTRIBUTE_ENTRY.reg())
				.add(AttributeRegistry.MAX_MANA, new AttrDispEntry(false, 50000, 0), false)
				.add(AttributeRegistry.MANA_REGEN, new AttrDispEntry(true, 50100, 0), false)
				.add(AttributeRegistry.COOLDOWN_REDUCTION, new AttrDispEntry(true, 50200, 0), false)
				.add(AttributeRegistry.SPELL_POWER, new AttrDispEntry(true, 50300, 0), false);
	}

	private void genSpell(Collector map) {
		var config = new SpellPriorityConfig();
		config.start(SpellRegistry.TELEKINESIS_SPELL.get()).weight(192).dist(12, 10, 2).mana(1, 3); // 念力
		config.start(SpellRegistry.COUNTERSPELL_SPELL.get()).mana(3, 5); // 法术反制
		config.start(SpellRegistry.SHIELD_SPELL.get()).weight(185).dist(6, 3).mana(1, 3).health(0.75, 0.5); // 护盾术
		config.start(SpellRegistry.FROSTBITE_SPELL.get()).weight(200).mana(1, 3).effect(MobEffectRegistry.FROSTBITTEN_STRIKES); // 霜噬
		config.start(SpellRegistry.ICE_TOMB_SPELL.get()).weight(200).dist(5, 10).mana(1, 2).health(1, 0.75); // 冰霜之墓
		config.start(SpellRegistry.CHARGE_SPELL.get()).weight(202).mana(1, 3).effect(MobEffectRegistry.CHARGED); // 超负荷
		config.start(SpellRegistry.ECHOING_STRIKES_SPELL.get()).weight(200).mana(1, 3).effect(MobEffectRegistry.ECHOING_STRIKES); // 回响打击
		config.start(SpellRegistry.EVASION_SPELL.get()).weight(200).mana(1, 3).effect(MobEffectRegistry.EVASION); // 末影闪避
		config.start(SpellRegistry.HEAL_SPELL.get()).weight(200).mana(1, 2).health(1, 0.75); // 治疗
		config.start(SpellRegistry.ABYSSAL_SHROUD_SPELL.get()).weight(200).mana(1, 3).effect(MobEffectRegistry.ABYSSAL_SHROUD); // 深渊庇佑
		config.start(SpellRegistry.HEARTSTOP_SPELL.get()).weight(200).mana(1, 3).effect(MobEffectRegistry.HEARTSTOP); // 止心术
		config.start(SpellRegistry.OAKSKIN_SPELL.get()).weight(200).mana(1, 3).effect(MobEffectRegistry.OAKSKIN); // 橡肤
		config.start(SpellRegistry.SPIDER_ASPECT_SPELL.get()).weight(200).mana(1, 3).effect(MobEffectRegistry.SPIDER_ASPECT); // 蛛毒之相
		config.start(SpellRegistry.SNOWBALL_SPELL.get()).weight(192).dist(10, 6).mana(1, 3).aoe(4, 0.5, 1); // 雪球术
		config.start(SpellRegistry.ACID_ORB_SPELL.get()).weight(192).dist(10, 6).mana(1, 3).aoe(3, 0, 1).targetEffect(MobEffectRegistry.REND); // 腐蚀喷吐
		config.start(SpellRegistry.SLOW_SPELL.get()).weight(192).dist(32, 30).mana(1, 3).targetEffect(MobEffectRegistry.SLOWED); // 迟缓
		config.start(SpellRegistry.HEALING_CIRCLE_SPELL.get()).weight(200).mana(1, 2).health(1, 0.75); // 治愈之环
		config.start(SpellRegistry.HASTE_SPELL.get()).weight(201).mana(1, 3).effect(MobEffectRegistry.HASTENED); // 急迫
		config.start(SpellRegistry.BLIGHT_SPELL.get()).weight(192).dist(32, 30).mana(1, 3).targetEffect(MobEffectRegistry.BLIGHT); // 枯萎术
		config.start(SpellRegistry.ROOT_SPELL.get()).weight(192).dist(32, 30).mana(1, 3); // 纠缠根须
		config.start(SpellRegistry.BLESSING_OF_LIFE_SPELL.get()).weight(200).mana(1, 2).health(1, 0.75); // 生命祝福
		config.start(SpellRegistry.FROSTWAVE_SPELL.get()).weight(192).dist(6.75, 5.75, 0.75).mana(1, 3).aoe(7, 0.75, 1).targetEffect(MobEffectRegistry.CHILLED); // 冰浪
		config.start(SpellRegistry.HEAT_SURGE_SPELL.get()).weight(192).dist(6, 5, 0.5).mana(1, 3).aoe(7, 0.5, 1).targetEffect(MobEffectRegistry.REND); // 焰涌
		config.start(SpellRegistry.CLEANSE_SPELL.get()).weight(200).mana(1, 2); // 净化
		config.start(SpellRegistry.FORTIFY_SPELL.get()).weight(200).mana(1, 2); // 神圣守护
		config.start(SpellRegistry.INVISIBILITY_SPELL.get()).weight(202).mana(1, 3).health(0.75, 0.5).effect(MobEffectRegistry.TRUE_INVISIBILITY); // 隐身术
		config.start(SpellRegistry.THUNDERSTORM_SPELL.get()).weight(200).mana(1, 3).aoe(8, 0, 0.2).effect(MobEffectRegistry.THUNDERSTORM); // 雷暴
		config.start(SpellRegistry.GREATER_HEAL_SPELL.get()).weight(201).mana(1, 2).health(0.75, 0.5); // 强效治疗
		config.start(SpellRegistry.BLAZE_STORM_SPELL.get()).dist(12, 8).mana(1, 2); // 烈焰风暴
		config.start(SpellRegistry.RAY_OF_SIPHONING_SPELL.get()).weight(110).dist(12, 8).mana(1, 2); // 血吸光束
		config.start(SpellRegistry.CONE_OF_COLD_SPELL.get()).weight(120).dist(10, 7).mana(1, 2).aoe(2, 0, 1); // 刺骨寒风
		config.start(SpellRegistry.FIRE_BREATH_SPELL.get()).weight(120).dist(10, 7).mana(1, 2).aoe(2, 0, 1); // 火焰吐息
		config.start(SpellRegistry.ELECTROCUTE_SPELL.get()).weight(120).dist(10, 7).mana(1, 2).aoe(2, 0, 1); // 电刑
		config.start(SpellRegistry.DRAGON_BREATH_SPELL.get()).weight(120).dist(10, 7).mana(1, 2).aoe(2, 0, 1); // 龙息
		config.start(SpellRegistry.POISON_BREATH_SPELL.get()).weight(120).dist(10, 7).mana(1, 2).aoe(2, 0, 1); // 毒雾喷射
		config.start(SpellRegistry.STARFALL_SPELL.get()).weight(105).dist(12, 8).mana(1, 2).aoe(6, 0, 1); // 星海落瀑
		config.start(SpellRegistry.LOB_CREEPER_SPELL.get()).weight(105).dist(10, 6).mana(1, 1.5).aoe(4, 0, 1); // 苦力怕迫击炮
		config.start(SpellRegistry.FLAMING_BARRAGE_SPELL.get()).dist(12, 8).mana(0.5, 1.5); // 炽焰追踪弹幕
		config.start(SpellRegistry.ELDRITCH_BLAST_SPELL.get()).weight(110).dist(30, 20).mana(0.5, 2.5); // 邪术冲击波
		config.start(SpellRegistry.RAY_OF_FROST_SPELL.get()).weight(110).dist(30, 20).mana(1, 2.5); // 冰霜射线
		config.start(SpellRegistry.ICE_SPIKES_SPELL.get()).dist(8, 4, 1.5).mana(1, 2.5); // 冰霜尖刺
		config.start(SpellRegistry.ICICLE_SPELL.get()).dist(64, 16).mana(1, 1.5); // 冰霜箭
		config.start(SpellRegistry.FIREBOLT_SPELL.get()).dist(64, 16).mana(1, 1.5); // 火焰箭
		config.start(SpellRegistry.BALL_LIGHTNING_SPELL.get()).dist(50, 10).mana(1, 1.5).aoe(2, 0, 1); // 闪电球
		config.start(SpellRegistry.MAGIC_MISSILE_SPELL.get()).dist(64, 16).mana(1, 1.5); // 魔法飞弹
		config.start(SpellRegistry.GUIDING_BOLT_SPELL.get()).weight(192).dist(64, 16).mana(1, 1.5).targetEffect(MobEffectRegistry.GUIDING_BOLT); // 曳光弹
		config.start(SpellRegistry.BLOOD_NEEDLES_SPELL.get()).dist(22, 16).mana(1, 1.5); // 猩红之刺
		config.start(SpellRegistry.BLOOD_SLASH_SPELL.get()).weight(105).dist(64, 16).mana(1, 1.5).aoe(2, 0, 1); // 猩红斩击
		config.start(SpellRegistry.WITHER_SKULL_SPELL.get()).dist(42, 10).mana(1, 1.5); // 凋灵之首
		config.start(SpellRegistry.FIRECRACKER_SPELL.get()).weight(110).dist(32, 30).mana(1, 1.5).aoe(2, 0, 1); // 烟火四射
		config.start(SpellRegistry.LIGHTNING_BOLT_SPELL.get()).weight(110).dist(64, 62).mana(1, 2.5).aoe(3, 0, 2); // 落雷
		config.start(SpellRegistry.CHAIN_LIGHTNING_SPELL.get()).weight(110).dist(4, 3, 0.5).mana(1, 2.5).aoe(3, 0.5, 2); // 连锁闪电
		config.start(SpellRegistry.SUNBEAM_SPELL.get()).weight(110).dist(48, 46).mana(1, 2.5).aoe(3, 0, 2); // 烈阳射线
		config.start(SpellRegistry.ACUPUNCTURE_SPELL.get()).weight(110).dist(32, 30).mana(1, 2.5); // 猩红刺狱
		config.start(SpellRegistry.DEVOUR_SPELL.get()).weight(110).dist(8, 6).mana(1, 2.5); // 嗜血啃咬
		config.start(SpellRegistry.MAGMA_BOMB_SPELL.get()).weight(105).dist(12, 8).mana(1, 2).aoe(4, 0, 3); // 岩浆炸弹
		config.start(SpellRegistry.THROW_SPELL.get()).weight(105).dist(10, 6).mana(1, 1.5); // 投掷
		config.start(SpellRegistry.LIGHTNING_LANCE_SPELL.get()).weight(105).dist(24, 16).mana(1, 2.5); // 雷鸣长枪
		config.start(SpellRegistry.POISON_ARROW_SPELL.get()).weight(105).dist(24, 16).mana(1, 2.5).aoe(2, 0, 1); // 毒箭射击
		config.start(SpellRegistry.RAISE_HELL_SPELL.get()).weight(120).dist(8, 6).mana(0.5, 3).aoe(7, 0, 4); // 地狱浮现
		config.start(SpellRegistry.SONIC_BOOM_SPELL.get()).weight(110).dist(20, 18, 5).mana(1, 3); // 音爆
		config.start(SpellRegistry.FLAMING_STRIKE_SPELL.get()).weight(185).dist(3, 2).mana(1, 2).aoe(2, 0, 1); // 炽焰斩击
		config.start(SpellRegistry.DIVINE_SMITE_SPELL.get()).weight(185).dist(4, 3).mana(1, 2).aoe(2, 0, 1); // 神圣打击
		config.start(SpellRegistry.GUST_SPELL.get()).weight(120).dist(7, 4).mana(1, 2); // 呼啸之风
		config.start(SpellRegistry.FANG_STRIKE_SPELL.get()).weight(110).dist(8, 6, 1).mana(1, 2.5); // 唤魔尖牙
		config.start(SpellRegistry.STOMP_SPELL.get()).weight(110).dist(5, 4, 1).mana(1, 2.5).aoe(4, 1, 3); // 践踏
		config.start(SpellRegistry.FIRE_ARROW_SPELL.get()).weight(105).dist(32, 16).mana(1, 2.5).aoe(2, 0, 1); // 爆裂炽焰箭
		config.start(SpellRegistry.FIREBALL_SPELL.get()).weight(105).dist(64, 20).mana(1, 3).aoe(3, 1, 4); // 火球术
		config.start(SpellRegistry.MAGIC_ARROW_SPELL.get()).weight(105).dist(64, 20).mana(1, 2.5); // 魔法箭
		config.start(SpellRegistry.BLACK_HOLE_SPELL.get()).weight(110).dist(8, 6, 2).mana(1, 4).aoe(6, 2, 5); // 黑洞
		config.start(SpellRegistry.ICE_BLOCK_SPELL.get()).weight(110).dist(48, 46).mana(1, 2).aoe(3, 0, 2); // 霜降
		config.start(SpellRegistry.SCORCH_SPELL.get()).weight(110).dist(32, 30).mana(1, 2).aoe(2.5, 0, 2); // 灼烧
		config.start(SpellRegistry.ARROW_VOLLEY_SPELL.get()).weight(105).dist(48, 46).mana(1, 2).aoe(2, 0.2, 1); // 万箭齐发
		config.start(SpellRegistry.CHAIN_CREEPER_SPELL.get()).weight(110).dist(48, 46).mana(1, 2.5).aoe(5, 0.2, 3); // 苦力怕之环
		config.start(SpellRegistry.FANG_WARD_SPELL.get()).weight(120).dist(2, 1, 0.3).mana(1, 2.5).aoe(1.5, 0.3, 1); // 尖牙之环
		config.start(SpellRegistry.WISP_SPELL.get()).weight(105).dist(48, 46).mana(1, 2); // 圣灵
		config.start(SpellRegistry.SCULK_TENTACLES_SPELL.get()).weight(110).dist(32, 30).mana(1, 3).aoe(2.5, 1.3, 2); // 幽匿之触
		config.start(SpellRegistry.EARTHQUAKE_SPELL.get()).weight(110).dist(32, 30).mana(1, 3).aoe(8, 0, 5); // 地震
		config.start(SpellRegistry.FIREFLY_SWARM_SPELL.get()).weight(105).dist(32, 30).mana(1, 2); // 萤火虫群
		config.start(SpellRegistry.POISON_SPLASH_SPELL.get()).weight(110).dist(32, 30).mana(1, 2).aoe(2, 0, 1); // 毒液飞溅
		config.start(SpellRegistry.SHOCKWAVE_SPELL.get()).weight(120).dist(9, 7, 1).mana(1, 3).aoe(8, 1, 5); // 震荡波
		config.start(SpellRegistry.FROST_STEP_SPELL.get()).weight(195).dist(12, 3).mana(1, 2); // 霜步
		config.start(SpellRegistry.BURNING_DASH_SPELL.get()).weight(195).dist(8, 4).mana(1, 2); // 烈焰冲锋
		config.start(SpellRegistry.ASCENSION_SPELL.get()).weight(195).dist(4, 2).mana(1, 2); // 飞升
		config.start(SpellRegistry.VOLT_STRIKE_SPELL.get()).weight(195).dist(12, 6).mana(1, 2); // 伏特打击
		config.start(SpellRegistry.SHADOW_SLASH.get()).weight(195).dist(12, 6).mana(1, 2); // 暗影斩击
		config.start(SpellRegistry.TELEPORT_SPELL.get()).weight(195).dist(12, 3).mana(1, 2); // 传送术
		config.start(SpellRegistry.BLOOD_STEP_SPELL.get()).weight(195).dist(12, 3).mana(1, 2); // 血步
		map.add(GolemMagicka.SPELL, GolemMagicka.loc("iron_spell"), config);
	}

}
