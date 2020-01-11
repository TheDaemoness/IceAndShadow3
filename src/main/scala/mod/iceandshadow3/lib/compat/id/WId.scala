package mod.iceandshadow3.lib.compat.id

import mod.iceandshadow3.lib.base.TNamed
import mod.iceandshadow3.lib.util.FnOptions
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistryEntry

class WId(protected[compat] val asVanilla: ResourceLocation) extends TNamed[WId] with Comparable[WId] {
	def this(what: IForgeRegistryEntry[_]) = this(what.getRegistryName)
	def this(namespace: String, name: String) = this(new ResourceLocation(namespace, name))
	def id = this
	final override def namespace: String = asVanilla.getNamespace
	final override def name: String = asVanilla.getPath
	final def nameFull: String = asVanilla.toString
	override def toString = nameFull

	override def compareTo(t: WId) = {
		if(t == null) 1
		else WId.compare(this, t)
	}
}
object WId {
	private val compare = new FnOptions[(WId, WId), Int, Int](
		pair => pair._1.namespace.compareTo(pair._2.namespace),
		pair => pair._1.name.compareTo(pair._2.name)
	) {
		override protected def discard(what: Int) = what == 0
		override protected def transform(what: Int) = what
		override protected def default = 0
	}
}
