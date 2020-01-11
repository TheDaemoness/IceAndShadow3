package mod.iceandshadow3.config

object Config {
	final class BadConfigException(message: String) extends Exception(message)
}
abstract class Config {
	private var sealedstate = false

	private[iceandshadow3] def seal(): Unit = {sealedstate = true}
	protected def isSealed = sealedstate

	def name: String
	def versionMinor: Int
	def versionMajor: Int
	def options: java.util.Set[String]
	
	@throws(classOf[IllegalArgumentException])
	@throws(classOf[Config.BadConfigException])
	def set(option: String, value: String): Boolean
	@throws(classOf[IllegalArgumentException])
	def get(option: String): String
	@throws(classOf[IllegalArgumentException])
	def getComment(option: String): String
}
