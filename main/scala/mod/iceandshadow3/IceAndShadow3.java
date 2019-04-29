package mod.iceandshadow3;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod(IceAndShadow3.MODID)
public class IceAndShadow3 {
	private static final Logger BEAVER = LogManager.getLogger();
	// The value here should match an entry in the META-INF/mods.toml file
	public static final String MODID = "iceandshadow3";

	public IceAndShadow3() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::initRegistries);
		bus.addListener(this::initCommon);
		bus.addListener(this::initClient);
		bus.addListener(this::enqueueIMC);
		bus.addListener(this::processIMC);
		bus.addListener(this::onServerStarting);

		MinecraftForge.EVENT_BUS.register(this);
		
		Init$.MODULE$.initEarly();
	}

	private void initRegistries(final RegistryEvent.NewRegistry event) {
		Init$.MODULE$.initRegistries();
	}
	
	private void initCommon(final FMLCommonSetupEvent event) {
		Init$.MODULE$.initCommon();
	}

	private void initClient(final FMLClientSetupEvent event) {
		Init$.MODULE$.initClient();
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		Init$.MODULE$.imcSend();
	}

	private void processIMC(final InterModProcessEvent event) {
		Init$.MODULE$.imcRecv(event.getIMCStream());
	}
	
	private void onServerStarting(FMLServerStartingEvent event) {
		Init$.MODULE$.serverStarting(event.getServer());
	}

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> reg) {
			Init$.MODULE$.registerBlocks(reg.getRegistry());
		}
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> reg) {
			Init$.MODULE$.registerItems(reg.getRegistry());
		}
	}
	
	public static Logger logger() {return BEAVER;}
}
