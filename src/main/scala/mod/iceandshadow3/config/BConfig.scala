package mod.iceandshadow3.config

import java.util.Set

sealed class BadConfigException(message: String) extends Exception(message)

abstract class BConfig {
	private var sealedstate = false;

	private[iceandshadow3] def seal() = {sealedstate = true}
	protected def isSealed() = sealedstate

	def name(): String
	def versionMinor(): Int
	def versionMajor(): Int
	def options(): Set[String]
	
	@throws(classOf[IllegalArgumentException])
	@throws(classOf[BadConfigException])
	def set(option: String, value: String): Boolean
	@throws(classOf[IllegalArgumentException])
	def get(option: String): String
	@throws(classOf[IllegalArgumentException])
	def getComment(option: String): String
}
