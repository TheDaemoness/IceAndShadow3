package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.block.WBlockView
import mod.iceandshadow3.compat.block.`type`.BlockTypeSimple
import mod.iceandshadow3.compat.entity.{WEntity, WEntityLiving}
import mod.iceandshadow3.compat.world.{TWWorld, WDimensionCoord, WWorld}
import mod.iceandshadow3.gen.BWorldSource
import mod.iceandshadow3.spatial.{IPosBlock, IPosChunk, IPosColumn, IVec3}
import mod.iceandshadow3.util.Color

abstract class BDimension(val name: String) extends BBiome {
	var isEnabled = false
	private var _coord: WDimensionCoord = _
	def coord = _coord
	def coord_= (where: WDimensionCoord): Unit = {_coord = where; isEnabled = true;}

	def getRespawnDim: WDimensionCoord
	def getWorldSpawn(world: TWWorld): IPosBlock
	def findSpawn(world: TWWorld, pos: IPosChunk): IPosColumn = new IPosColumn {
		override def xBlock = 0
		override def zBlock = 0
	}
	def findSpawn(world: TWWorld, pos: IPosColumn): IPosBlock = getWorldSpawn(world)
	def checkSpawn(block: WBlockView): Boolean = true

	def getSkyBrightness(partialTicks: Float): Float
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
	def handleEscape(who: WEntity, where: WDimensionCoord): Boolean = true

	def getWorldSource(seed: Long): BWorldSource

	def brightnessTable(lightBrightnessTable: Array[Float]): Unit
  def modifyLightmap(lightSky: Float, lightBlock: Float, color: Color): Color = color

	def onEntityLivingUpdate(who: WEntityLiving): Unit = {}
}
