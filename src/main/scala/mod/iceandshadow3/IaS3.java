package mod.iceandshadow3;

import mod.iceandshadow3.config.ConfigManager;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** The mod class for Ice and Shadow III. If you're looking for main, this is roughly it.
 */
@SuppressWarnings("unused")
@Mod(IaS3.MODID)
public class IaS3 {
	/// The value here should match the entry in the META-INF/mods.toml file
	public static final String MODID = "iceandshadow3";
	public static final int VER_CFG_FMT = 1;
	private static final Logger BEAVER = LogManager.getLogger();
	private static final Level BUG_LEVEL = Level.forName("BUG", 150);

	private static ConfigManager<ConfigClient> cfgClient;
	private static ConfigManager<ConfigServer> cfgServer;

	private static boolean weIsClient = false;

	private static InitCommon$ init = InitCommon$.MODULE$;

	public IaS3() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		//net.minecraftforge.fml.event.lifecycle event handlers here ONLY!
		bus.addListener(this::initRegistries);
		bus.addListener(this::initCommon);
		bus.addListener(this::initClient);
		bus.addListener(this::enqueueIMC);
		bus.addListener(this::processIMC);
		bus.addListener(this::initFinal);

		MinecraftForge.EVENT_BUS.register(this);

		init.populateBinders();
		init.initEarly();
	}

	private void initRegistries(final RegistryEvent.NewRegistry event) {
		InitModSynergy$.MODULE$.makeRegistries();
	}
	
	private void initCommon(final FMLCommonSetupEvent event) {
		init.initLate();
		getCfgServer();
	}

	private void initClient(final FMLClientSetupEvent event) {
		cfgClient = new ConfigManager<>(new ConfigClient());
		cfgClient.get().seal();
		weIsClient = true;
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		InitModSynergy$.MODULE$.imcSend();
	}

	private void processIMC(final InterModProcessEvent event) {
		InitModSynergy$.MODULE$.imcRecv(event.getIMCStream());
	}

	private void initFinal(final FMLLoadCompleteEvent event) {
		if(weIsClient) init.initFinalClient();
		else init.initFinalServer();
	}

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> reg) {
			init.registerBlocks(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> reg) {
			init.registerItems(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityType<?>> reg) {
			init.registerEntities(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerPots(final RegistryEvent.Register<Effect> reg) {
			init.registerPots(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerParticles(final RegistryEvent.Register<ParticleType<?>> reg) {
			init.registerParticles(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerSounds(final RegistryEvent.Register<SoundEvent> reg) {
			init.registerSounds(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerBiomes(final RegistryEvent.Register<Biome> reg) {
			init.registerBiomes(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerDimensions(final RegistryEvent.Register<ModDimension> reg) {
			init.registerDimensions(reg.getRegistry());
		}
	}

	@SubscribeEvent
	public void onServerLoading(FMLServerAboutToStartEvent event) {
		getCfgServer().seal();
		if(event.getServer().isDedicatedServer()) {
			BEAVER.error("Ice and Shadow III is currently UNSTABLE on dedicated servers. YOU HAVE BEEN WARNED!");
		}
	}

	@SubscribeEvent
	public void onRegisterDimensions(RegisterDimensionsEvent event) {
		init.enableDimensions();
	}

	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		if(event.getServer().isDedicatedServer()) {
			init.primeDimensions(event.getServer());
		}
	}

	@SubscribeEvent
	public void onServerStopped(FMLServerStoppedEvent event) {
		cfgServer.close();
		cfgServer = null;
	}
	
	public static Logger logger() {return BEAVER;}
	public static void bug(Object what, Object... args) {
		StringBuilder sb = new StringBuilder();
		sb.append("Please make sure the IaS3 developers know about the following problem:");
		if(args.length > 0) sb.append("\n");
		for(Object o: args) sb.append(o.toString());
		final String message = sb.toString();

		class SomebodyScrewedThePooch extends Exception {
			private SomebodyScrewedThePooch(String msg) {super(msg);}
		}
		if(what == null) what = new SomebodyScrewedThePooch("Caller of bug(...)");

		if(what instanceof Throwable) BEAVER.log(BUG_LEVEL, message, (Throwable)what);
		else {
			String whoScrewedThePooch;
			if(what instanceof Class) whoScrewedThePooch = ((Class) what).getTypeName();
			else if(what instanceof String) whoScrewedThePooch = (String)what;
			else whoScrewedThePooch = what.getClass().getTypeName();
			BEAVER.log(BUG_LEVEL, message, new SomebodyScrewedThePooch(whoScrewedThePooch));
		}
	}

	public static ConfigServer getCfgServer() {
		if(cfgServer == null) {
			cfgServer = new ConfigManager<>(new ConfigServer());
		}
		return cfgServer.get();
	}
	@OnlyIn(Dist.CLIENT)
	public static ConfigClient getCfgClient() {return cfgClient.get();}
}
