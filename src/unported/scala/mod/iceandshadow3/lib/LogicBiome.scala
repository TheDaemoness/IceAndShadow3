package mod.iceandshadow3.lib

import mod.iceandshadow3.lib.util.Color

abstract class LogicBiome {
	def baseDownfall: Float
	def baseTemperature: Float
	def baseWaterColor: Color = Color.BLUE
	def baseWaterFogColor: Color = Color.BLUE
	def baseAltitude: Float
	def baseHilliness: Float
}
