package mod.iceandshadow3.lib.compat.misc

import mod.iceandshadow3.lib.compat.ServerAnalyses
import net.minecraft.server.MinecraftServer

class ServerAnalyzer[In, Out](protected val fn: MinecraftServer => In => Out)
extends (MinecraftServer => In => Out) {
	def apply(server: MinecraftServer) = fn(server)
	def enable() = ServerAnalyses.add(this)
}
class ServerAnalyzerDerived[In, Meta, Out](
	protected val source: ServerAnalyzer[In, Meta],
	protected val fn2: (In => Meta) => In => Out
) extends ServerAnalyzer[In, Out](serv => in => fn2(ServerAnalyses(serv)(source))(in))
