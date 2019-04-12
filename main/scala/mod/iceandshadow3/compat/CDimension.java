package mod.iceandshadow3.compat;

import mod.iceandshadow3.Config;
import mod.iceandshadow3.IceAndShadow3;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

///Pseudo-enumeration and view into DimensionData.
public class CDimension {
	private static java.util.HashMap<Integer, CDimension> dims = new java.util.HashMap<>();
	public static CDimension
	OVERWORLD = new CDimension(0),
	NETHER = new CDimension(-1),
	END = new CDimension(1),
	NYX = new CDimension(); //TODO: Move out of here.
	static CDimension get(int id) {
		CDimension retval = dims.get(id);
		if(retval == null && DimensionManager.isDimensionRegistered(id)) retval = new CDimension(id);
		return retval;
	}
	
	private boolean registered;

	/**
	 * Constructor for vanilla dimensions and already-registered dimensions.
	 */
	private CDimension(int id) {
		dims.put(id, this);
		registered = true;
	}
	CDimension() {
		registered = false;
	}
	public void register(Config.IDimensionConfig cfg, String name, String suffix) {
		if(registered) return;
		if(DimensionManager.isDimensionRegistered(cfg.getDimId())) {
			final int oldid = cfg.getDimId();
			cfg.setDimId(DimensionManager.getNextFreeDimId());
			IceAndShadow3.logger().error("Dimension id "+oldid+" for dimension \"" +name+ "\" is taken. Changing to next available id: "+cfg.getDimId());
		}
		//TODO: The following really shouldn't pass WorldProvider.class
		DimensionManager.registerDimension(cfg.getDimId(), DimensionType.register(name, suffix, cfg.getDimId(), WorldProvider.class, false));
		dims.put(cfg.getDimId(), this);
		registered = true;
	}
}
