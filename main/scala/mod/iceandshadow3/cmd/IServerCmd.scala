package mod.iceandshadow3.cmd

import java.util.List

import mod.iceandshadow3.compat.CServerCmdSender;

trait IServerCmd {
	def getName(): String
	def getArgsUsage(sendername: String): List[String]
	/**
	 * @return A non-null object that will be passed to run.
	 * If the returned object is null, it'll be assumed that there was a usage screw-up.
	 */
	def parse(args: List[String]): Object
	/**
	 * @param parsed The object returned by parse.
	 * @return A string that will be wrapped and thrown if an error occurred, or null otherwise.
	 */
	def run(parsed: Object, c: CServerCmdSender): String
	
	def getAutocomplete(args: List[String], sendername: String): List[String] = null
	def isUsername(args: List[String], index: Int): Boolean = false;
	def getArgsMin(): Int = 0;
	def getArgsMax(): Int = getArgsMin()
}