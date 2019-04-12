package mod.iceandshadow3;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Config.*;

@net.minecraftforge.common.config.Config(modid = ModInfo.MODID)
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
    
    public static interface IDimensionConfig {
    	public int getDimId();
    	public void setDimId(int realid);
    }
    public static class NyxConfig implements IDimensionConfig {
    	@Name("Dimension ID")
    	@Comment("The default dimension ID for Nyx")
    	@RequiresMcRestart
    	public int dimid = -816;

		@Override
		public int getDimId() {return dimid;}

		@Override
		public void setDimId(int realid) {dimid = realid;}
    }

    @Name("Nyx Options")
    public static NyxConfig nyx = new NyxConfig();
    
    public static void sync() {
    	ConfigManager.sync(ModInfo.MODID, Type.INSTANCE);
    }
}
