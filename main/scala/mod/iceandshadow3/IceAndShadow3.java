package mod.iceandshadow3;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SaveInspectionHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.RegistryBuilder;

import java.io.File;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;

@Mod(modid = ModInfo.MODID,
	name = ModInfo.NAME,
	version = ModInfo.V_STRING,
	acceptedMinecraftVersions = ModInfo.MCVER_RANGE,
	acceptableSaveVersions = ModInfo.V_RANGE_SAVE,
	acceptableRemoteVersions = ModInfo.V_RANGE_REMOTE,
	updateJSON = "https://raw.githubusercontent.com/TheDaemoness/IceAndShadow3/master/update.json",
	dependencies = ""
	)
public class IceAndShadow3 {

    private static Logger beaver;
    
	@SubscribeEvent
	public void onConfigChangedEvent(OnConfigChangedEvent event) {
		if (ModInfo.MODID.equals(event.getModID())) Config.sync();
	}

	@SubscribeEvent
	public void registerRegistry(RegistryEvent.NewRegistry event) {
		Init$.MODULE$.initRegistries(event);
	}
    
	@EventHandler
    public void init1(FMLPreInitializationEvent event) {
		Config.sync();
        beaver = event.getModLog();
        if(ModInfo.UNSTABLE) {
        	beaver.warn("This copy of IaS3 is a development version, and may be unstable. YOU HAVE BEEN WARNED!");
        	event.getModMetadata().version = (ModInfo.BRANCH != null) ? ModInfo.BRANCH : ModInfo.V_STRING;
        } else event.getModMetadata().version = ModInfo.V_STRING;
        Init$.MODULE$.initEarly(event.getSide());
        MinecraftForge.EVENT_BUS.register(this);
    }
    @EventHandler
    public void init2(FMLInitializationEvent event) {
        Init$.MODULE$.initMid(event.getSide());
    }
    @EventHandler
    public void messageForYouSir(IMCEvent event) {
    	for(IMCMessage msg : event.getMessages()) {
    		Init$.MODULE$.msg(msg);
    	}
    }
    @EventHandler
    public void init3(FMLPostInitializationEvent event) {
        Init$.MODULE$.initLate(event.getSide());
    }
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		Init$.MODULE$.serverStarting(event);
	}
    
    public static Logger logger() {return beaver;}
}

