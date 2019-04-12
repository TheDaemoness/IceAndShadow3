package mod.iceandshadow3

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.relauncher.Side

object Init {
	def initEarly(arg: Side): Unit = {}
	def initRegistries(arg: RegistryEvent.NewRegistry): Unit = {}
	//def register(arg: RegistryEvent.Register[]): Unit = {} //Going to want several of these.
	def initMid(arg: Side): Unit = {}
	def msg(arg: IMCMessage): Unit = {}
	def initLate(arg: Side): Unit = {}
	def serverStarting(arg: FMLServerStartingEvent): Unit = {}
}
