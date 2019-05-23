package mod.iceandshadow3.forge

import mod.iceandshadow3.forge.bait._
import net.minecraftforge.eventbus.api.IEventBus

object SEventFisherman {
  var triggered = false
  def baitHooks(bus: IEventBus): Unit = {
    if(triggered) return else triggered = true
    new EventBaitOwnerDeath(bus)
  }
}
