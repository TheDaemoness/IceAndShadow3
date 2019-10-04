package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.gen.{BWorldGenRegion, BWorldGenRegionTerrain, Cellmaker3d}
import mod.iceandshadow3.lib.spatial.{Cells, IPosColumn, PairXZ}

class NyxRegionTerrain(coord: PairXZ, val noises: NoisesNyx)
extends BWorldGenRegionTerrain(coord) {
	import BWorldGenRegionTerrain.width

	private val islemap = noises.isleMaker.apply(xFrom, zFrom, width, width)
	val smoothsnow = IaS3.getCfgServer.smooth_snow.get

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
	//lazy val stoneMapUpper = noises.stonemakerUpper.apply(xFrom, zFrom, width, width)
	private lazy val stoneMapLower = noises.stonemakerLower.apply(xFrom, zFrom, width, width)

	private def map3d(in: Cellmaker3d, limit: Int) = {
		in(xFrom, 0, zFrom, width, limit, width, Cells.distance)
	}

	def isle(x: Int, z: Int) = islemap(x, z)

	def fissure(x: Int, y: Int, z: Int) = {
		val fA = fissuremapA(x, y, z)
		Math.min(fA*fA, fissuremapB(x, y, z))
	}

	def cave(x: Int, y: Int, z: Int) = {
		cavemapA(x, y, z) * cavemapB(x, y, z)
	}

	def stoneLower(x: Int, z: Int) = stoneMapLower(x, z)

	def scale(x: Int, z: Int) = Math.sqrt(1-heightmaps.scale(x,z))
	def crater(x: Int, z: Int) = heightmaps.crater(x,z)
	def ridge(x: Int, z: Int) = heightmaps.ridge(x, z)
	def hill(x: Int, z: Int) = heightmaps.hill(x, z)
	def mountain(x: Int, z: Int) = heightmaps.mountain(x, z)

	override def columnTf(where: IPosColumn) = {
		val xBlock = where.xBlock
		val zBlock = where.zBlock
		val cellres = isle(xBlock, zBlock)
		if(1-Cells.distance(cellres) <= 0.15) BWorldGenRegion.BColumnTransformer.NO_OP
		else {
			val cell = cellres.cellClosest
			if (cell.x == 0 && cell.z == 0) new NyxColumnIsleCentral(xBlock, zBlock, this)
			else new NyxColumnIsleMountainSnowyUsual(xBlock, zBlock, this)
		}
	}
}
