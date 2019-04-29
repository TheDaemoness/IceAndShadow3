package mod.iceandshadow3.compat;

import javax.annotation.Nullable;

public class HarvestMethod {
	private static java.util.HashMap<String, HarvestMethod> lookups;
	public static HarvestMethod
		PICKAXE = new HarvestMethod("pickaxe"),
		SHOVEL = new HarvestMethod("shovel"), //Let's not have a spade repeat.
		AXE = new HarvestMethod("axe"),
		BLADE = new HarvestMethod("sword"),
		SHEAR = new HarvestMethod("shear", null);
	public static HarvestMethod get(String name) {
		HarvestMethod retval = lookups.get(name);
		if(retval == null) retval = new HarvestMethod(name);
		return retval;
	}
	
	protected String name;
	private HarvestMethod(String string) {
		this(string, string);
	}
	private HarvestMethod(String name, @Nullable String classname) {
		this.name = name;
		if(classname != null) lookups.putIfAbsent(classname, this);
	}
	
	@Override
	public String toString() {return name;}
}
