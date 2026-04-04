package dev.xkmc.golemmagicka.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.golemmagicka.content.entity.GolemSpellInfoToClient;
import dev.xkmc.golemmagicka.content.entity.GolemSpellManager;
import dev.xkmc.golemmagicka.init.data.*;
import dev.xkmc.golemmagicka.init.reg.GMItems;
import dev.xkmc.golemmagicka.init.reg.GMModifiers;
import dev.xkmc.golemmagicka.init.reg.GMTypes;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2serial.network.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GolemMagicka.MODID)
@EventBusSubscriber(modid = GolemMagicka.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GolemMagicka {

	public static final String MODID = "golemmagicka";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			MODID, 1,
			e -> e.create(GolemSpellInfoToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT)
	);

	public GolemMagicka() {
		GMTypes.register();
		GMItems.register();
		GMModifiers.register();
		GMConfig.init();
	}


	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			GolemSpellManager.init();
		});
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.LANG, GMLang::genLang);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, GMRecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(GMTagGen.SPELL_TAGS, GMTagGen::genSpellTag);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, GMConfigGen::genDataMap);

		var gen = event.getGenerator();
		var output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		var server = event.includeServer();
		gen.addProvider(server, new GMConfigGen(gen, pvd));
	}


	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
