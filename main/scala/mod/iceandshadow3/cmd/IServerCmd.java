package mod.iceandshadow3.cmd;

import java.util.List;

import mod.iceandshadow3.compat.CServerCmdSender;


public interface IServerCmd {
	public String getName();
	public List<String> getArgsUsage(String sendername);
	/**
	 * @return A non-null object that will be passed to run.
	 * If the returned object is null, it'll be assumed that there was a usage screw-up.
	 */
	public Object parse(List<String> args);
	/**
	 * @param parsed The object returned by parse.
	 * @return A string that will be wrapped and thrown if an error occurred, or null otherwise.
	 */
	public String run(Object parsed, CServerCmdSender c);
	
	public default List<String> getAutocomplete(List<String> args, String sendername) {return null;}
	public default boolean isUsername(List<String> args, int index) {return false;}
	public default int getArgsMin() {return 0;}
	public default int getArgsMax() {return getArgsMin();}
}
