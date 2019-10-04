package mod.iceandshadow3.lib.spatial

abstract class BWorldRegion(val xFrom: Int, val zFrom: Int, val xMax: Int, val zMax: Int) {
	final def xWidth: Int = xMax - xFrom + 1
	final def zWidth: Int = zMax - zFrom + 1
	override def toString = s"[$xFrom, $zFrom] to [$xMax, $zMax]"
}
