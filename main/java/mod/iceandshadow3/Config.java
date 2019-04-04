package mod.iceandshadow3;

import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.*;

@net.minecraftforge.common.config.Config(modid = IceAndShadow3.MODID)
public class Config {
    @Name("Death System")
	@Comment({"Use an alternate death system on IaS dimensions.","Disable if using a grave mod."})
	public static boolean deathsys = true;
    @Name("Brutal Mode")
	@Comment("Increase difficulty.")
	public static boolean brutal = false;
    @Name("Low Particles")
	@Comment("Force reduced particles, even on fancy graphics.")
	public static boolean lowfx = false;
    
    public static void sync() {
    	ConfigManager.sync(IceAndShadow3.MODID, Type.INSTANCE);
    }
}
