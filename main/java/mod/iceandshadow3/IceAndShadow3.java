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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.RegistryBuilder;

import java.io.File;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.event.RegistryEvent;

@Mod(modid = IceAndShadow3.MODID,
	name = IceAndShadow3.NAME,
	version = IceAndShadow3.V_STRING,
	acceptedMinecraftVersions = "1.12.2",
	acceptableSaveVersions = IceAndShadow3.V_RANGE_SAVE,
	acceptableRemoteVersions = IceAndShadow3.V_RANGE_REMOTE,
	updateJSON = "",
	dependencies=""
	)
public class IceAndShadow3 {
	public static final String MODID = "iceandshadow3";
	public static final String NAME = "Ice and Shadow III";
	public static final String BRANCH = "Unstable";
	public static final int V_REV = 0;
	public static final int V_MAJOR = 0;
	public static final int V_MINOR = 0;
	public static final int V_PATCH = 0;
	
	@SuppressWarnings("unused")
	public static final boolean UNSTABLE = BRANCH != null || V_REV == 0 || V_MAJOR == 0;
	public static final String V_STRING_SHORT = V_REV+"."+V_MAJOR+"."+V_MINOR;
	public static final String V_STRING = V_STRING_SHORT+"."+V_PATCH;
	private static final String V_SAVE_MIN = V_REV+".0.0.0";
	private static final String V_SAVE_MAX = V_REV+"."+V_MAJOR+"."+(V_MINOR+1)+".0";
	private static final String V_REMOTE_MIN = V_REV+"."+V_MAJOR+'.'+V_MINOR+".0";
	private static final String V_REMOTE_MAX = V_REV+"."+(V_MAJOR+1)+".0.0";
	public static final String V_RANGE_SAVE = "["+V_SAVE_MIN+','+V_SAVE_MAX+')';
	public static final String V_RANGE_REMOTE = "["+V_REMOTE_MIN+','+V_REMOTE_MAX+')';

    private static Logger beaver;
    
    @EventHandler
    public void onConfigChangedEvent(OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) Config.sync();
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
    }
	@EventHandler
	public void registerRegistry(RegistryEvent.NewRegistry event) {
		DispatchInit.initRegistries();
	}
	@EventHandler
	public void register(RegistryEvent.Register event) {
		DispatchInit.register(event);
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
		// TODO: Server commands.
	}
    
    public static Logger logger() {return beaver;}
}
