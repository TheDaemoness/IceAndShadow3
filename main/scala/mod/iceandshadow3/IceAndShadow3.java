package mod.iceandshadow3;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mod.iceandshadow3.compat.block.CRegistryBlock;
import mod.iceandshadow3.compat.item.CRegistryItem;
import mod.iceandshadow3.config.BConfig;
import mod.iceandshadow3.config.ConfigManager;

import java.io.File;
import java.util.stream.Collectors;

@Mod(IceAndShadow3.MODID)
public class IceAndShadow3 {
	/// The value here should match an entry in the META-INF/mods.toml file
	public static final String MODID = "iceandshadow3";
	public static final int VER_CFG_FMT = 1;
	private static final Logger BEAVER = LogManager.getLogger();
	public static final Level BUG_LEVEL = Level.forName("INTERNAL", 150);
		
	private ConfigManager<ConfigClient> cfgClient;
	private ConfigManager<ConfigServer> cfgServer;

	public IceAndShadow3() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::initRegistries);
		bus.addListener(this::initCommon);
		bus.addListener(this::initClient);
		bus.addListener(this::enqueueIMC);
		bus.addListener(this::processIMC);
		bus.addListener(this::onServerStarting);
		bus.addListener(this::onServerStopped);

		MinecraftForge.EVENT_BUS.register(this);
		
		Domains.initEarly();
	}

	private void initRegistries(final RegistryEvent.NewRegistry event) {
		ModSynergy$.MODULE$.makeRegistries();
	}
	
	private void initCommon(final FMLCommonSetupEvent event) {
		Domains.initLate();
		configServer();
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
		configServer().seal();
	}
	
	private void onServerStopped(FMLServerStoppedEvent event) {
		cfgServer.close();
		cfgServer = null;
	}

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> reg) {
			Domains.registerBlocks(new CRegistryBlock(reg.getRegistry()));
		}
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> reg) {
			Domains.registerItems(new CRegistryItem(reg.getRegistry()));
		}
	}
	
	public static Logger logger() {return BEAVER;}
	public static void bug(Object... args) {BEAVER.log(BUG_LEVEL,args);}
	
	@OnlyIn(Dist.CLIENT)
	public ConfigClient configClient() {return cfgClient.get();}
	public ConfigServer configServer() {
		if(cfgServer == null) {
			cfgServer = new ConfigManager<>(new ConfigServer());
		}
		return cfgServer.get();
	}
}
