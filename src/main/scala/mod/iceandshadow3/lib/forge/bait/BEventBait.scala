package mod.iceandshadow3.lib.forge.bait

import mod.iceandshadow3.lib.base.{BLogic, LogicTriad}
import mod.iceandshadow3.lib.compat.util.TWLogical
import mod.iceandshadow3.lib.forge.BEventHandler
import mod.iceandshadow3.lib.forge.fish.TEventFish
import net.minecraftforge.eventbus.api.{Event, EventPriority, IEventBus}

import scala.reflect.{ClassTag, classTag}

abstract class BEventBait[EventType <: Event :ClassTag] extends BEventHandler {
	def priority = EventPriority.NORMAL
	def receiveCancelled = true

	override def register(bus: IEventBus): Unit = {
		bus.addListener[EventType](
			priority,
			receiveCancelled,
			classTag[EventType].runtimeClass.asInstanceOf[Class[EventType]],
			(it: EventType) => handle(it)
		)
	}

	protected def handle(event: EventType): Unit

	protected def forEventFish[FishType <: TEventFish: ClassTag, L <: BLogic, T](
		ref: TWLogical[L], fn: (LogicTriad[L], FishType) => T): Option[T] =
	{
		ref.getLogicTriad.foreach(triad => {
			triad.logic.getEventFish[FishType](triad.variant).foreach(feesh => {
				return Some(fn(triad, feesh))
			})
		})
		None
	}
}
