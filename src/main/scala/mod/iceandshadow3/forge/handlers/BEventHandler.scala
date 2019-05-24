package mod.iceandshadow3.forge.handlers

import net.minecraftforge.eventbus.api.IEventBus

class BEventHandler {
	def register(bus: IEventBus): Unit = bus.register(this)
}
