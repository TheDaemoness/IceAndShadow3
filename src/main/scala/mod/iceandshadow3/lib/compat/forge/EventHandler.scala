package mod.iceandshadow3.lib.compat.forge

import net.minecraftforge.eventbus.api.IEventBus

class EventHandler {
	def register(bus: IEventBus): Unit = bus.register(this)
}
