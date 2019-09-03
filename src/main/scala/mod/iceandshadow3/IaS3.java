package mod.iceandshadow3;

import mod.iceandshadow3.config.ConfigManager;
import mod.iceandshadow3.lib.compat.Registrar$;
import mod.iceandshadow3.lib.compat.misc.BServerAnalysis;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/** The mod class for Ice and Shadow III. If you're looking for main, this is roughly it.
 */
@SuppressWarnings("unused")
@Mod(IaS3.MODID)
public class IaS3 {
	/// The value here should match the entry in the META-INF/mods.toml file
	public static final String MODID = "iceandshadow3";
	public static final int VER_CFG_FMT = 1;
	private static AtomicBoolean didBug = new AtomicBoolean(false);
	private static final Logger BEAVER = LogManager.getLogger();
	private static final Level BUG_LEVEL = Level.forName("BUG", 150);

	private static ConfigManager<ConfigClient> cfgClient;
	private static ConfigManager<ConfigServer> cfgServer;

	private static boolean weIsClient = false;

	private static InitCommon$ init = InitCommon$.MODULE$;

	private static boolean shouldInit = true;

	/** Tool mode is a partial initialization of IaS3, suitable for testing or tooling. */
	public static class ToolMode {
		private static boolean _toolmode = false;
		public static boolean isActive() {
			return _toolmode;
		}
		public static void init() {
			if(shouldInit) {
				_toolmode = true;
				shouldInit = false;
				init.initToolMode();
				init.initEarly();
			} else if(!_toolmode) {
				IaS3.bug("Caller of init", "Attempted to enter tool mode after IaS3 was fully initialized.");
			}
		}
	}

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

		if(!shouldInit) logger().warn("IaS3 initialization attempted more than once.");
		else {
			shouldInit = false;
			init.initNormalNode();
			init.initEarly();
		}
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
		ContentLists.purge();
	}

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		private static Registrar$ registrar = Registrar$.MODULE$;
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> reg) {
			registrar.registerBlocks(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> reg) {
			registrar.registerItems(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityType<?>> reg) {
			registrar.registerEntities(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerPots(final RegistryEvent.Register<Effect> reg) {
			registrar.registerPots(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerParticles(final RegistryEvent.Register<ParticleType<?>> reg) {
			registrar.registerParticles(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerSounds(final RegistryEvent.Register<SoundEvent> reg) {
			registrar.registerSounds(reg.getRegistry());
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
			init.enableDimensions();
		}
	}

	@SubscribeEvent
	public void analyzeServer(FMLServerStartedEvent event) {
		ServerAnalyses$.MODULE$.set(event.getServer());
	}

	@SubscribeEvent
	public void onRegisterDimensions(RegisterDimensionsEvent event) {
		init.enableDimensions();
	}

	@SubscribeEvent
	public void onServerStopped(FMLServerStoppedEvent event) {
		ServerAnalyses$.MODULE$.clear();
		cfgServer.close();
		cfgServer = null;
	}
	
	public static Logger logger() {return BEAVER;}
	public static boolean bugged() { return didBug.get(); }
	public static void bug(Object what, Object... args) {
		didBug.set(true);
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
