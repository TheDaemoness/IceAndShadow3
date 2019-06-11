package mod.iceandshadow3.basics

import mod.iceandshadow3.Multiverse
import mod.iceandshadow3.compat.block.`type`.BlockTypeSimple
import mod.iceandshadow3.compat.dimension.WDimensionCoord
import mod.iceandshadow3.compat.entity.WEntity
import mod.iceandshadow3.compat.world.{WSound, WWorld}
import mod.iceandshadow3.gen.BWorldSource
import mod.iceandshadow3.spatial.{IPosChunk, IPosColumn, IVec3}
import mod.iceandshadow3.util.Color

abstract class BDimension(val name: String) extends BBiome {
	Multiverse.addDimension(this)
	var isEnabled = false
	private var _coord: WDimensionCoord = _
	def coord = _coord
	def coord_= (where: WDimensionCoord): Unit = {_coord = where; isEnabled = true;}

	def getSkyBrightness(partialTicks: Float): Float
	def getWorldSpawn: IVec3
	def findSpawn(where: IPosChunk, check: Boolean): IVec3
	def cloudLevel: Float
	def seaLevel: Int
	def peakLevel: Int = 256
	def hasFogAt(where: IPosColumn): Boolean
	def skyAngle(worldTime: Long, partialTicks: Float): Float
	def fogColor(skyAngle: Float, partialTicks: Float): Color

	def defaultLand(): BlockTypeSimple
	def defaultSea(): BlockTypeSimple

	override def baseAltitude = seaLevel/128f
	override def baseHilliness = (peakLevel/seaLevel)/128f

	def handleArrival(world: WWorld, who: WEntity): IVec3
	def handleEscape(who: WEntity, where: WDimensionCoord): Boolean = false

	def getWorldSource(seed: Long): BWorldSource

	def brightnessTable(lightBrightnessTable: Array[Float]): Unit
  def modifyLightmap(lightSky: Float, lightBlock: Float, color: Color): Color = color
}
