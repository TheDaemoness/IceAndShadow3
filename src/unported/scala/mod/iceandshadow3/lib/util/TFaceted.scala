package mod.iceandshadow3.lib.util

trait TFaceted[T] {
	def facet[What <: T: scala.reflect.ClassTag]: Option[What]
}
