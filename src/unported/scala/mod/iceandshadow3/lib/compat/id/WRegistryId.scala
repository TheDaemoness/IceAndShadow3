package mod.iceandshadow3.lib.compat.id

import net.minecraft.util.ResourceLocation

abstract class WRegistryId[+Return](vanilla: ResourceLocation)
extends WId(vanilla) with Iterable[Return] {
	def get: Option[Return]
	@throws[NoSuchElementException]
	def getOrThrow: Return = get.getOrElse(throw new NoSuchElementException(s"Cannot dereference $this"))
	override def iterator = get.iterator
	override def foreach[U](f: Return => U): Unit = get.foreach(f)
	override def map[B](f: Return => B) = get.map(f)
	final override def toString = nameFull
	protected def translationKeyPrefix: String
	def translationKey: String = s"$translationKeyPrefix.$namespace.$name"
}
