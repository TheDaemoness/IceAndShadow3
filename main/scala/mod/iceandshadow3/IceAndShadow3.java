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

import mod.iceandshadow3.compat.block.CRegistryBlock;
import mod.iceandshadow3.compat.item.CRegistryItem;

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
		
		Domains.initEarly();
	}

	private void initRegistries(final RegistryEvent.NewRegistry event) {
		ModSynergy$.MODULE$.makeRegistries();
	}
	
	private void initCommon(final FMLCommonSetupEvent event) {
		Domains.initLate();
	}

	private void initClient(final FMLClientSetupEvent event) {
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		ModSynergy$.MODULE$.imcSend();
	}

	private void processIMC(final InterModProcessEvent event) {
		ModSynergy$.MODULE$.imcRecv(event.getIMCStream());
	}
	
	private void onServerStarting(FMLServerStartingEvent event) {
		
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
}
