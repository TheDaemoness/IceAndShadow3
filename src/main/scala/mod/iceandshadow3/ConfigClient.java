package mod.iceandshadow3;

import mod.iceandshadow3.config.ConfigReflective;
import mod.iceandshadow3.data.DatumBool;

//IMPORTANT: DO NOT TRANSLATE TO SCALA!
public class ConfigClient extends ConfigReflective {
	@Override public String name() {return "client";}
	@Override public int versionMajor() {return 1;}
	@Override public int versionMinor() {return 0;}
	
	@Entry(comment="Reduces particle effects even when it would result in a gameplay disadvantage.")
	public DatumBool low_particles = new DatumBool(false);
}
