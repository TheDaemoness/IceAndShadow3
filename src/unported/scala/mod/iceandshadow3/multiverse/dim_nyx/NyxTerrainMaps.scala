package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.gen.Cellmaker3d
import mod.iceandshadow3.lib.spatial.{Cells, TupleXYZ}
import mod.iceandshadow3.multiverse.dim_nyx.column.{ColumnFnNyx, ColumnFnNyxDivide, ColumnFnNyxIsleCentral, ColumnFnNyxIsleMountainUsual}

import scala.collection.mutable

class NyxTerrainMaps(val noises: NoisesNyx, xFrom: Int, zFrom: Int, width: Int)
extends ((Int, Int) => ColumnFnNyx) {
	private val islemap = noises.isleMaker.apply(xFrom, zFrom, width, width)

	private def map3d(in: Cellmaker3d, limit: Int) = {
		in(xFrom, 0, zFrom, width, limit, width, Cells.distance)
	}

	private lazy val fissuremapA = map3d(noises.fissuremakerMinor, WorldGenNyx.yFissureMax)
	private lazy val fissuremapB = map3d(noises.fissuremakerMajor, WorldGenNyx.yFissureMax)
	private lazy val cavemapA = map3d(noises.cavemakerA, WorldGenNyx.yCaveMax)
	private lazy val cavemapB = map3d(noises.cavemakerB, WorldGenNyx.yCaveMax)

	private lazy val heightmaps = new Object {
		val ridge = noises.noisemakerRidge.apply(xFrom, zFrom, width, width)
		val crater = noises.noisemakerDip.apply(xFrom, zFrom, width, width)
		val scale = noises.noisemakerRidgeScale.apply(xFrom, zFrom, width, width)
		val hill = noises.noisemakerHills.apply(xFrom, zFrom, width, width)
		val mountain = noises.noisemakerMountain.apply(xFrom, zFrom, width, width)
	}
	private lazy val stoneMaker = noises.stonemakerLower.apply(xFrom, zFrom, width, width,
		new (Cells.Result => LivingstoneTypeSource.Sources) {
			private val map = new mutable.HashMap[TupleXYZ, LivingstoneTypeSource.Sources]
			override def apply(key: Cells.Result) = {
				map.getOrElseUpdate(key.cellClosest,
					new LivingstoneTypeSource.Sources(key.makeRandomXYZ(noises.seed, 41889))
				)
			}
		}
	)

	def isle(x: Int, z: Int) = islemap(x, z)

	def fissure(x: Int, y: Int, z: Int) = {
		val fA = fissuremapA(x, y, z)
		Math.min(fA*fA, fissuremapB(x, y, z))
	}

	def cave(x: Int, y: Int, z: Int) = cavemapA(x, y, z) * cavemapB(x, y, z)

	def stoneLower(x: Int, z: Int) = stoneMaker(x, z)

	def scale(x: Int, z: Int) = 1-heightmaps.scale(x,z)
	def crater(x: Int, z: Int) = heightmaps.crater(x,z)
	def ridge(x: Int, z: Int) = heightmaps.ridge(x, z)
	def hill(x: Int, z: Int) = heightmaps.hill(x, z)
	def mountain(x: Int, z: Int) = heightmaps.mountain(x, z)

	def apply(xBlock: Int, zBlock: Int): ColumnFnNyx = {
		new ColumnFnNyxIsleCentral(xBlock, zBlock, this)
		val cellres = isle(xBlock, zBlock)
		if(1-Cells.distance(cellres) <= 0.15) new ColumnFnNyxDivide(cellres)
		else {
			val cell = cellres.cellClosest
			if (cell.x == 0 && cell.z == 0) new ColumnFnNyxIsleCentral(xBlock, zBlock, this)
			else new ColumnFnNyxIsleMountainUsual(xBlock, zBlock, this)
		}
	}
}
