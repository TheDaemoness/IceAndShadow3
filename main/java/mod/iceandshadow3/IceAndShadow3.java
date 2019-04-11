package mod.iceandshadow3;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SaveInspectionHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
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

@Mod(modid = IceAndShadow3.MODID,
	name = IceAndShadow3.NAME,
	version = IceAndShadow3.V_STRING,
	acceptedMinecraftVersions = IceAndShadow3.MCVER_RANGE,
	acceptableSaveVersions = IceAndShadow3.V_RANGE_SAVE,
	acceptableRemoteVersions = IceAndShadow3.V_RANGE_REMOTE,
	updateJSON = "https://raw.githubusercontent.com/TheDaemoness/IceAndShadow3/master/update.json",
	dependencies = ""
	)
public class IceAndShadow3 {
	public static final String MODID = "iceandshadow3";
	public static final String NAME = "Ice and Shadow III";
	public static final String BRANCH = "Unstable"; //Set null if this is not a dev/unstable variant.
	public static final int MCVER_MINOR = 12;
	public static final int MCVER_PATCH_MIN = 2;
	public static final int MCVER_PATCH_MAX = 2;
	public static final int V_REV = 0;
	public static final int V_MAJOR = 0;
	public static final int V_MINOR = 0;
	public static final int V_PATCH = 0;

	@SuppressWarnings("unused")
	public static final boolean UNSTABLE = BRANCH != null || V_REV == 0 || V_MAJOR == 0;
	public static final String V_STRING_SHORT = V_REV+"."+V_MAJOR+"."+V_MINOR;
	public static final String V_STRING = V_STRING_SHORT+"."+V_PATCH;
	
	private static final String MCVER_START = "1."+MCVER_MINOR+"."+MCVER_PATCH_MIN;
	private static final String MCVER_END = "1."+MCVER_MINOR+"."+(MCVER_PATCH_MAX+1);
	private static final String V_SAVE_START = V_REV+".0.0.0";
	private static final String V_SAVE_END = V_REV+"."+V_MAJOR+"."+(V_MINOR+1)+".0";
	private static final String V_REMOTE_START = V_REV+"."+V_MAJOR+'.'+V_MINOR+".0";
	private static final String V_REMOTE_END = V_REV+"."+(V_MAJOR+1)+".0.0";
	
	public static final String MCVER_RANGE = '['+MCVER_START+","+MCVER_END+')';
	public static final String V_RANGE_SAVE = "["+V_SAVE_START+','+V_SAVE_END+')';
	public static final String V_RANGE_REMOTE = "["+V_REMOTE_START+','+V_REMOTE_END+')';

    private static Logger beaver;
    
	@SubscribeEvent
	public void onConfigChangedEvent(OnConfigChangedEvent event) {
		if (event.getModID().equals(MODID)) Config.sync();
	}

	@SubscribeEvent
	public void registerRegistry(RegistryEvent.NewRegistry event) {
		DispatchInit.initRegistries();
	}

	@SubscribeEvent
	public void register(RegistryEvent.Register event) {
		DispatchInit.fillRegister(event);
		DispatchSynergy.fillRegister(event);
	}
    
	@EventHandler
    public void init1(FMLPreInitializationEvent event) {
		Config.sync();
        beaver = event.getModLog();
        if(UNSTABLE) {
        	beaver.warn("This copy of IaS3 is a development version, and may be unstable. YOU HAVE BEEN WARNED!");
        	event.getModMetadata().version = (BRANCH != null) ? BRANCH : V_STRING;
        } else event.getModMetadata().version = V_STRING;
        DispatchInit.initEarly(event.getSide());
        MinecraftForge.EVENT_BUS.register(this);
    }
    @EventHandler
    public void init2(FMLInitializationEvent event) {
        DispatchInit.initLate(event.getSide());
        DispatchSynergy.initEarly(event.getSide());
    }
    @EventHandler
    public void messageForYouSir(IMCEvent event) {
    	DispatchSynergy.msg(event.getMessages());
    }
    @EventHandler
    public void init3(FMLPostInitializationEvent event) {
        DispatchSynergy.initLate(event.getSide());
    }
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		DispatchInit.addCommands(event);
	}
    
    public static Logger logger() {return beaver;}
}
