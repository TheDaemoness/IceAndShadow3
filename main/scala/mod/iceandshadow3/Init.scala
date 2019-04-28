package mod.iceandshadow3

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent
import net.minecraft.server.MinecraftServer
import net.minecraftforge.fml.InterModComms

object Init {
	def initEarly(): Unit = {} //TODO: Domain construction.
	def initCommon(): Unit = {}
	def initClient(): Unit = {}
	def imcSend(): Unit = {}
	def initRegistries(): Unit = {} //TODO: RegistryBuilder
	//def register(arg: RegistryEvent.Register[]): Unit = {} //Going to want several of these.
	def imcRecv(arg: java.util.stream.Stream[InterModComms.IMCMessage]): Unit = {}
	def serverStarting(server: MinecraftServer): Unit = {}
}
