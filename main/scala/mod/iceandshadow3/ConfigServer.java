package mod.iceandshadow3;

import mod.iceandshadow3.config.ConfigReflective;
import mod.iceandshadow3.data.DatumBool;

public class ConfigServer extends ConfigReflective {
	@Override public String name() {return "server";}
	@Override public int versionMajor() {return 1;}
	@Override public int versionMinor() {return 0;}
	
	@Entry(comment="Greatly increases IaS3's difficulty.")
	public DatumBool brutal_mode = new DatumBool(false);
}
