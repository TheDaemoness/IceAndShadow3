package mod.iceandshadow3.multiverse.dim_nyx.column

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.block.`type`.BlockTypeSnow
import mod.iceandshadow3.lib.util.MathUtils
import mod.iceandshadow3.multiverse.dim_nyx.WorldGenNyx.{yBald, yThinning}
import mod.iceandshadow3.multiverse.dim_nyx.{NyxTerrainMaps, WorldGenNyx}

abstract class ColumnFnNyxIsleMountain(x: Int, z: Int, val chunk: NyxTerrainMaps)
extends ColumnFnNyxIsle(chunk.isle(x, z)) {
	override protected def stoneUpper: WBlockState = WorldGenNyx.stones(0)

	override protected def genHeight() = Math.min(253f, {
		val scale = Math.sqrt(chunk.scale(x, z))
		var cratervalue = chunk.crater(x, z).toDouble
		cratervalue *= Math.cbrt(cratervalue)/(4-scale)
		val mountainvalue = islevalue*(1-Math.cbrt(Math.cos(scale*chunk.mountain(x,z)*Math.PI)))/2
		val ridgevalue = (1-Math.cbrt(Math.cos(scale*chunk.ridge(x,z)*Math.PI)))/2
		var hillvalue = MathUtils.sinelike(1-chunk.hill(x,z))
		hillvalue *= hillvalue
		val tuner = if(islevalue <= 0.3) (islevalue-0.2)*10 else 1d
		val totalmountainvalue = (ridgevalue+mountainvalue)*tuner*(1+islevalue)+hillvalue
		val base = 1.5+MathUtils.sinelike(islevalue)+totalmountainvalue+cratervalue
		if(islevalue <= 0.2) base*MathUtils.sinelike((islevalue-0.15)*20)
		else base
	}.toFloat*32)
}
