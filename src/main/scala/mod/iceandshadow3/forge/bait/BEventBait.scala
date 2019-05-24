package mod.iceandshadow3.forge.bait

import mod.iceandshadow3.basics.util.LogicTriad
import mod.iceandshadow3.compat.{BCRef, BLogic}
import mod.iceandshadow3.forge.fish.IEventFish
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

	protected def forEventFish[FishType <: IEventFish: ClassTag, L <: BLogic, T](
		ref: BCRef[L], fn: (LogicTriad[L], FishType) => T): Option[T] =
	{
		ref.getLogicTriad.foreach(triad => {
			triad.logic.getEventFish[FishType](triad.variant).foreach(feesh => {
				return Some(fn(triad, feesh))
			})
		})
		None
	}
}
