package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.block.BlockType
import mod.iceandshadow3.util.Color

abstract class BBiome {
	def baseDownfall: Float
	def baseTemperature: Float
	def baseWaterColor: Color = Color.BLUE
	def baseWaterFogColor: Color = Color.BLUE
	def baseAltitude: Float
	def baseHilliness: Float
}
