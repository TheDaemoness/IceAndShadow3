package mod.iceandshadow3;

public class ModInfo {
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
}
