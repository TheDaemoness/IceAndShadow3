package mod.iceandshadow3.lib.compat.forge.bait

import mod.iceandshadow3.lib.base.{LogicCommon, ProviderLogic}
import mod.iceandshadow3.lib.compat.util.TWLogical
import mod.iceandshadow3.lib.compat.forge.EventHandler
import mod.iceandshadow3.lib.compat.forge.fish.TEventFish
import net.minecraftforge.eventbus.api.{Event, EventPriority, IEventBus}

import scala.reflect.{ClassTag, classTag}

abstract class EventBait[EventType <: Event :ClassTag] extends EventHandler {
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

	protected def forEventFish[FishType <: TEventFish: ClassTag, L <: LogicCommon, T](
		ref: TWLogical[L] with ProviderLogic[L], fn: (L, FishType) => T): Option[T] =
	{
		Option(ref.getLogic).foreach(logic =>
			logic.facet[FishType].foreach(feesh => {
				return Some(fn(logic, feesh))
			})
		)
		None
	}
}
