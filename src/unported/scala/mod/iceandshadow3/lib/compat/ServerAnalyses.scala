package mod.iceandshadow3.lib.compat

import javax.annotation.Nonnull
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.misc.ServerAnalyzer
import net.minecraft.server.MinecraftServer

import scala.collection.mutable

object ServerAnalyses {
	private val analyzers = new mutable.HashSet[ServerAnalyzer[_, _]]
	private val servers = new mutable.HashMap[MinecraftServer, ServerAnalyses]
	def add(what: ServerAnalyzer[_, _]): ServerAnalyses.type = {
		analyzers += what
		servers.foreach(_._2.apply(what))
		this
	}
	protected[iceandshadow3] def apply[T](@Nonnull what: MinecraftServer): ServerAnalyses =
		servers.getOrElseUpdate(what, {
			IaS3.logger().debug("Adding analysis for server "+what)
			val sa = new ServerAnalyses(what)
			for(analyzer <- analyzers) sa.apply(analyzer)
			sa
		})
	protected[iceandshadow3] def remove(what: MinecraftServer) = servers.remove(what)
}
class ServerAnalyses private(server: MinecraftServer) {
	private val map = new mutable.HashMap[ServerAnalyzer[_, _], Any]
	def apply[In, Out](what: ServerAnalyzer[In, Out]): In => Out =
		map.getOrElseUpdate(what, what(server)).asInstanceOf[In => Out]
}
