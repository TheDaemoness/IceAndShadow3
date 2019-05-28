package mod.iceandshadow3;

import mod.iceandshadow3.compat.block.CRegistryBlock;
import mod.iceandshadow3.compat.item.CRegistryItem;
import mod.iceandshadow3.compat.world.CSound$;
import mod.iceandshadow3.config.ConfigManager;
import mod.iceandshadow3.forge.SEventFisherman$;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** The mod class for Ice and Shadow III. If you're looking for main, this is roughly it.
 */
@Mod(IaS3.MODID)
public class IaS3 {
	/// The value here should match the entry in the META-INF/mods.toml file
	public static final String MODID = "iceandshadow3";
	public static final int VER_CFG_FMT = 1;
	private static final Logger BEAVER = LogManager.getLogger();
	private static final Level BUG_LEVEL = Level.forName("BUG", 150);

	private static ConfigManager<ConfigClient> cfgClient;
	private static ConfigManager<ConfigServer> cfgServer;

	public IaS3() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::initRegistries);
		bus.addListener(this::initCommon);
		bus.addListener(this::initClient);
		bus.addListener(this::enqueueIMC);
		bus.addListener(this::processIMC);
		bus.addListener(this::onServerStarting);
		bus.addListener(this::onServerStopped);

		MinecraftForge.EVENT_BUS.register(this);
		
		Multiverse.initEarly();
	}

	private void initRegistries(final RegistryEvent.NewRegistry event) {
		ModSynergy$.MODULE$.makeRegistries();
	}
	
	private void initCommon(final FMLCommonSetupEvent event) {
		Multiverse.initLate();
		SEventFisherman$.MODULE$.baitHooks();
		getCfgServer();
	}

	private void initClient(final FMLClientSetupEvent event) {
		cfgClient = new ConfigManager<>(new ConfigClient());
		cfgClient.get().seal();
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		ModSynergy$.MODULE$.imcSend();
	}

	private void processIMC(final InterModProcessEvent event) {
		ModSynergy$.MODULE$.imcRecv(event.getIMCStream());
	}
	
	private void onServerStarting(FMLServerStartingEvent event) {
		getCfgServer().seal();
		Multiverse.registerDimensions();
	}
	
	private void onServerStopped(FMLServerStoppedEvent event) {
		cfgServer.close();
		cfgServer = null;
	}

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> reg) {
			Multiverse.registerBlocks(new CRegistryBlock(reg.getRegistry()));
		}
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> reg) {
			Multiverse.registerItems(new CRegistryItem(reg.getRegistry()));
		}
		@SubscribeEvent
		public static void registerSounds(final RegistryEvent.Register<SoundEvent> reg) {
			CSound$.MODULE$.registerSounds(reg.getRegistry());
		}
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
