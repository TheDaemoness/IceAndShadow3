package mod.iceandshadow3

import mod.iceandshadow3.lib.compat.misc.BServerAnalysis
import net.minecraft.server.MinecraftServer

import scala.collection.mutable

object ServerAnalyses {
	private val analyzers = new mutable.HashSet[BServerAnalysis[_]]
	private val map = new mutable.HashMap[BServerAnalysis[_], Any]
	def add(what: BServerAnalysis[_]): Unit = analyzers += what
	def apply[T](what: BServerAnalysis[T]): T = map.getOrElse(what, null).asInstanceOf[T]
	private[iceandshadow3] def clear(): Unit = map.clear()
	private[iceandshadow3] def set(server: MinecraftServer): Unit = {
		clear() //Probably redundant, but shouldn't hurt.
		for(analyzer <- analyzers) {
			map.put(analyzer, analyzer(server))
		}
	}
}
