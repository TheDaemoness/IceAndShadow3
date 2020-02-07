package mod.iceandshadow3

import net.fabricmc.api.{ClientModInitializer, DedicatedServerModInitializer, ModInitializer}

class IaS3 extends ModInitializer with ClientModInitializer with DedicatedServerModInitializer {
	override def onInitialize(): Unit = {
		IaS3.logger.debug("Begin IaS3 common init")
	}

	override def onInitializeClient(): Unit = {
		IaS3.logger.debug("Begin IaS3 client init")
	}

	override def onInitializeServer(): Unit = {
		IaS3.logger.debug("Begin IaS3 server init")
	}
}
object IaS3 {
	import org.apache.logging.log4j.LogManager
	private val beaver = LogManager.getLogger(classOf[IaS3])
	def logger = beaver
}
