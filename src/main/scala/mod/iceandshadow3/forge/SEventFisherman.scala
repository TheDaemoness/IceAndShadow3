package mod.iceandshadow3.forge

import java.lang.reflect.InvocationTargetException

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.forge.bait._
import mod.iceandshadow3.forge.handlers._
import mod.iceandshadow3.forge.handlers.BEventHandler
import net.minecraftforge.common.MinecraftForge

object SEventFisherman {
  private val eventClasses: Set[Class[_ <: BEventHandler]] = Set(
    classOf[EventBaitOwnerDeath],
    classOf[EventBaitOwnerToss],
    classOf[EventHandlerWayfinderGiver]
  )
  var triggered = false
  def baitHooks(): Unit = {
    val bus = MinecraftForge.EVENT_BUS
    if(triggered) return else triggered = true
    for(classe <- eventClasses) try {
      classe.getConstructor().newInstance().register(bus)
    } catch {
      case e: NoSuchMethodException =>
        IaS3.bug(classe, "No default constructor in event handler.")
      case e: InvocationTargetException =>
        IaS3.bug(e.getCause, classe.getSimpleName+" threw on construction.")
      case e: ExceptionInInitializerError =>
        IaS3.bug(e.getCause, "Initialization due to "+classe.getSimpleName+" threw.")
      case e: SecurityException =>
        IaS3.logger().error("Security threw a hissy fit in SEventFisherman: "+e.getMessage)
      case e: Exception =>
        IaS3.bug(e, classe.getSimpleName+" couldn't be default-constructed.")
    }
  }
}
