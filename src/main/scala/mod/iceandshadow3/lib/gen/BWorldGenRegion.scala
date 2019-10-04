package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.spatial.{BWorldRegion, IPosColumn}

object BWorldGenRegion {
	val columnFnNoop: WorldGenColumn => Unit = _ => ()
	def columnFnBasic(yFrom: Int, yMax: Int, what: TBlockStateSource) = (col: WorldGenColumn) => {
		var y = yFrom
		while(y <= yMax) {
			col.update(y, what.asWBlockState)
			y += 1
		}
	}
}
abstract class BWorldGenRegion(xFrom: Int, zFrom: Int, xMax: Int, zMax: Int)
extends BWorldRegion(xFrom, zFrom, xMax, zMax) {
	def this(xFrom: Int, zFrom: Int, maxOffset: Int) = this(xFrom, zFrom, xFrom+maxOffset, zFrom+maxOffset)

	type F <: WorldGenColumn => Unit
	def apply(where: IPosColumn): F
}
