package mod.iceandshadow3.lib

import mod.iceandshadow3.lib.compat.block.WBlockView
import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.compat.entity.{WEntity, WEntityLiving}
import mod.iceandshadow3.lib.compat.world.{TWWorld, WDimensionCoord, WWorld}
import mod.iceandshadow3.lib.gen.BWorldGen
import mod.iceandshadow3.lib.spatial.{IPosBlock, IPosChunk, IPosColumn, IVec3}
import mod.iceandshadow3.lib.util.{Color, E3vl}

abstract class BDimension(val name: String) extends BBiome {
	private var _coord: WDimensionCoord = WDimensionCoord.VOID
	def isEnabled = _coord != WDimensionCoord.VOID
	def coord = _coord
	def coord_= (where: WDimensionCoord): Unit = {_coord = Option(where).getOrElse(WDimensionCoord.VOID)}

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

	def defaultLand(): TBlockStateSource
	def defaultSea(): TBlockStateSource

	override def baseAltitude = seaLevel/128f
	override def baseHilliness = (peakLevel/seaLevel)/128f

	def handleArrival(world: WWorld, who: WEntity) = true
	def handleEscape(who: WEntity, where: WDimensionCoord): Boolean = true
	def defaultPlacer(where: WWorld): IVec3

	def getWorldGen(seed: Long): BWorldGen

	def brightnessTable(lightBrightnessTable: Array[Float]): Unit
  def modifyLightmap(lightSky: Float, lightBlock: Float, color: Color): Color = color

	def onEntityLivingUpdate(who: WEntityLiving): Unit = {}
}
