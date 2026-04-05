package dev.xkmc.golemmagicka.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.golemmagicka.content.entity.GolemCDToClient;
import dev.xkmc.golemmagicka.content.entity.GolemSpellInfoToClient;
import dev.xkmc.golemmagicka.content.entity.GolemSpellManager;
import dev.xkmc.golemmagicka.init.data.*;
import dev.xkmc.golemmagicka.init.reg.GMItems;
import dev.xkmc.golemmagicka.init.reg.GMModifiers;
import dev.xkmc.golemmagicka.init.reg.GMTypes;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GolemMagicka.MODID)
@Mod.EventBusSubscriber(modid = GolemMagicka.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GolemMagicka {

	public static final String MODID = "golemmagicka";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);
	public static final IEventBus MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			new ResourceLocation(GolemMagicka.MODID, "main"), 1,
			e -> e.create(GolemSpellInfoToClient.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(GolemCDToClient.class, NetworkDirection.PLAY_TO_CLIENT)
	);

	public GolemMagicka() {
		GMTypes.register();
		GMItems.register();
		GMModifiers.register();
		GMConfig.init();
	}

	@SubscribeEvent
	public static void modifyAttributes(EntityAttributeModificationEvent event) {
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

		var gen = event.getGenerator();
		var output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		var server = event.includeServer();
		gen.addProvider(server, new GMConfigGen(gen));
	}


	public static ResourceLocation loc(String id) {
		return new ResourceLocation(MODID, id);
	}

}
