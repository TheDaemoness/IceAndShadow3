package mod.iceandshadow3.lib.forge.bait

import mod.iceandshadow3.lib.base.{BLogic, LogicPair, LogicProvider}
import mod.iceandshadow3.lib.compat.util.TWLogical
import mod.iceandshadow3.lib.forge.BEventHandler
import mod.iceandshadow3.lib.forge.fish.TEventFish
import net.minecraftforge.eventbus.api.{Event, EventPriority, IEventBus}

import scala.reflect.{ClassTag, classTag}

abstract class BEventBait[EventType <: Event :ClassTag] extends BEventHandler {
	def priority = EventPriority.NORMAL
	def receiveCancelled = true

	protected def handle(event: EventType): Unit

	override def register(bus: IEventBus): Unit = {
		bus.addListener[EventType](
			priority,
			receiveCancelled,
			classTag[EventType].runtimeClass.asInstanceOf[Class[EventType]],
			handle _
		)
	}

	protected def forEventFish[FishType <: TEventFish: ClassTag, L <: BLogic, T](
		ref: TWLogical[L] with LogicProvider[L], fn: (LogicPair[L], FishType) => T): Option[T] =
	{
		Option(ref.getLogicPair).foreach(pair =>
			pair.logic.facet[FishType].foreach(feesh => {
				return Some(fn(pair, feesh))
			})
		)
		None
	}
}
