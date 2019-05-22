package mod.iceandshadow3.forge.bait

import net.minecraftforge.eventbus.api.{Event, EventPriority, IEventBus}

import scala.reflect.{ClassTag, classTag}

abstract class BEventBait[EventType <: Event :ClassTag](bus: IEventBus) {
  def priority = EventPriority.NORMAL
  def receiveCancelled = true

  protected def handle(event: EventType)
  bus.addListener[EventType](
    priority,
    receiveCancelled,
    classTag[EventType].runtimeClass.asInstanceOf[Class[EventType]],
    (it: EventType) => handle(it)
  )
}
