package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.block.BBlockFn
import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.spatial.{IRegion3d, ITupleXZ, TupleXYZ}
import mod.iceandshadow3.lib.util.MathUtils
import mod.iceandshadow3.lib.util.collect.FixedMap2d

/** A mutable class that stores block information in a cuboid for the purposes of world gen structures. */
final class CanvasFeature protected(val domain: BDomain, val yWidth: Int, blocks: FixedMap2d[CanvasColumn])
extends BWorldGenFeatureTypeSimple[TWorldGenColumnFn, TWorldGenColumnFn](blocks.xWidth, blocks.zWidth) with IRegion3d {
	private class Column(val x: Int, val z: Int) {
		private val column = blocks(MathUtils.bound(0, x, xMax), MathUtils.bound(0, z, zMax))
		def apply(y: Int) = column(y)
		def update(y: Int, what: TBlockStateSource) = column(y).asWBlockState
		def transform(y: Int, fn: BBlockFn): Column = {
			column(y) = fn(x, y, z, column(y).asWBlockState)
			this
		}
	}

	override def xFrom = 0
	def yFrom = 0
	override def zFrom = 0
	override def xMax = xWidth-1
	def yMax = yWidth-1
	override def zMax = zWidth-1

	def this(domain: BDomain, xWidth: Int, yWidth: Int, zWidth: Int) = this(
		domain, yWidth,
		new FixedMap2d[CanvasColumn](0, 0, xWidth, zWidth, (x,z) => new CanvasColumn(domain, yWidth))
	)

	//The reason why TupleXZ and TupleXYZ are used here is because the IPos coordinates are absolute.
	//These have the same units as IPosBlock, but are relative to something else.

	/** Applies the provided function to one block. Usually inefficient. */
	def one(fn: BBlockFn, a: TupleXYZ): Unit = {
		new Column(a.x, a.z).transform(a.y, fn)
	}

	/** Applies the provided function to every block in the specified region*/
	def cuboid(fn: BBlockFn, where: IRegion3d): Unit = {
		val xStart = Math.max(xFrom, where.xFrom)
		val yStart = Math.max(yFrom, where.yFrom)
		val zStart = Math.max(zFrom, where.zFrom)
		val xEnd = Math.min(xMax, where.xMax)
		val yEnd = Math.min(yMax, where.yMax)
		val zEnd = Math.min(zMax, where.zMax)
		val it = new TupleXYZ(xStart, yStart, zStart)
		while(it.x <= xEnd) {
			it.z = zStart
			while(it.z <= zEnd) {
				val col = new Column(it.x, it.z)
				it.y = yStart
				while(it.y <= yEnd) {
					col.transform(it.y, fn)
					it.y += 1
				}
				it.z += 1
			}
			it.x += 1
		}
	}

	/** Appies the provided function to the specified blocks in the same column. */
	def column(fn: BBlockFn, coord: ITupleXZ, ys: Iterable[Int]): Unit = {
		val col = new Column(coord.x, coord.z)
		for(y <- ys) col.transform(y, fn)
	}

	override def columnAt(xRela: Int, zRela: Int, parent: TWorldGenColumnFn) = blocks(xRela, zRela)
}