package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.spatial.IPosColumn


abstract class BWorldGenRegionStructure[F <: WorldGenColumn => Unit](
	val origin: IPosColumn,
	xDeltaNeg: Int, zDeltaNeg: Int,
	xDeltaPos: Int, zDeltaPos: Int,
) extends BWorldGenRegion (
	origin.xBlock - xDeltaNeg, origin.zBlock - zDeltaNeg,
	origin.xBlock + xDeltaPos, origin.zBlock + zDeltaPos
) {
	def this(coord: IPosColumn, xDelta: Int, zDelta: Int) = this(coord, xDelta, zDelta, xDelta, zDelta)
	def this(coord: IPosColumn, radius: Int) = this(coord, radius, radius, radius, radius)
}
