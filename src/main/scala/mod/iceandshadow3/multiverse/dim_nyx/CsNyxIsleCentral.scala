package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.gen.Cellmaker.Result
import mod.iceandshadow3.util.MathUtils
import mod.iceandshadow3.util.collect.{FixedMap2d, UniSeq}

class CsNyxIsleCentral(noises: NoisesNyx, cells: FixedMap2d[Result])
	extends BCsNyxIsleMountain(noises, cells)
{
	override def genHeight(islevalue: Double, x: Int, z: Int) = {
		val bias = Math.max(islevalue*4 - 3, 0d)
		MathUtils.interpolate(super.genHeight(islevalue, x, z), bias, 128).toFloat
	}

	override def caveGen(height: Float, x: Int, z: Int): Seq[Boolean] =
		new UniSeq(256, false)
}
