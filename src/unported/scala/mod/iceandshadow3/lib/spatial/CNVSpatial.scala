package mod.iceandshadow3.lib.spatial

import scala.language.implicitConversions

object CNVSpatial {
	def fromDoubles(x: Double, y: Double, z: Double): IVec3 = new IVec3 {
		override def xRaw = IVec3.fromDouble(x)
		override def yRaw = IVec3.fromDouble(y).toInt
		override def zRaw = IVec3.fromDouble(z)
		override def xDouble = x
		override def yDouble = y
		override def zDouble = z
	}
}
