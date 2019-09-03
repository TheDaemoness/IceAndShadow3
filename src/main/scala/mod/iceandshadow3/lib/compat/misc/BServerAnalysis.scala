package mod.iceandshadow3.lib.compat.misc

import net.minecraft.server.MinecraftServer

abstract class BServerAnalysis[T] {
	def apply(server: MinecraftServer): T
}
