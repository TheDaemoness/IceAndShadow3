package mod.iceandshadow3.lib.compat.forge

import java.lang.reflect.InvocationTargetException

//TODO: The extracted event handlers shouldn't need to be manually registered here.

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.forge._
import mod.iceandshadow3.lib.compat.forge.bait._
import net.minecraftforge.common.MinecraftForge

object EventFisherman {
  private val eventClasses: Set[Class[_ <: EventHandler]] = Set(
    classOf[EventBaitOwnerDeath],
    classOf[EventBaitOwnerToss],
    classOf[EventHandlerEnderChest],
    classOf[EventHandlerDimension],
    classOf[EventHandlerNyx],
    classOf[EventHandlerStatus]
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
        IaS3.logger().error(s"Security threw a hissy fit in $this: "+e.getMessage)
      case e: Exception =>
        IaS3.bug(e, classe.getSimpleName+" couldn't be default-constructed.")
    }
  }
}
