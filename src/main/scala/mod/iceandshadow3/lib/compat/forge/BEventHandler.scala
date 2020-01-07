package mod.iceandshadow3.lib.compat.forge

import net.minecraftforge.eventbus.api.IEventBus

class BEventHandler {
	def register(bus: IEventBus): Unit = bus.register(this)
}
