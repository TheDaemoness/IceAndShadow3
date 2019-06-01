package mod.iceandshadow3;

import mod.iceandshadow3.config.ConfigReflective;
import mod.iceandshadow3.data.DatumBool;

//IMPORTANT: DO NOT TRANSLATE TO SCALA!
public class ConfigServer extends ConfigReflective {
	@Override public String name() {return "server";}
	@Override public int versionMajor() {return 1;}
	@Override public int versionMinor() {return 2;}
	
	@Entry(comment="Greatly increases IaS3's difficulty.")
	public DatumBool brutal_mode = new DatumBool(false);

	@Entry(comment="Be able to getColumn the wayfinder in other non-respawn dimensions, including the Nether.")
	public DatumBool early_wayfinder = new DatumBool(false);

	@Entry(comment="Use snow to reduce blockiness in terrain.")
	public DatumBool smooth_snow = new DatumBool(false);
}
