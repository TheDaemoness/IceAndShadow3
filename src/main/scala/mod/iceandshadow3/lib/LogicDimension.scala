package mod.iceandshadow3.lib

import mod.iceandshadow3.lib.compat.block.WBlockView
import mod.iceandshadow3.lib.compat.entity.{WEntity, WEntityLiving, WEntityPlayer}
import mod.iceandshadow3.lib.compat.world.{TWWorld, WDimensionCoord, WWorld}
import mod.iceandshadow3.lib.gen.WorldGen
import mod.iceandshadow3.lib.spatial.{IPosBlock, IPosChunk, IPosColumn, IVec3}
import mod.iceandshadow3.lib.util.Color
import mod.iceandshadow3.lib.world._

abstract class LogicDimension(val name: String) extends LogicBiome {
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

	def cloudLevel: Float
	def seaLevel: Int
	def peakLevel: Int = 256
	val handlerFog: BHandlerFog
	val handlerSky: BHandlerSky

	/** Whether this is a "surface" world or not. Returning true enables a lot of default behaviors. */
	def isSurface: Boolean
	override def baseAltitude = seaLevel/128f
	override def baseHilliness = (peakLevel/seaLevel)/128f

	def onArrivalPre(world: WWorld, who: WEntity) = true
	def onArrivalPost(who: WEntityPlayer): Unit = ()
	def onDeparture(who: WEntity, where: WDimensionCoord): Boolean = true
	def defaultPlace(where: WWorld): IVec3

	def getWorldGen(seed: Long): WorldGen

	def brightnessTable(lightBrightnessTable: Array[Float]): Unit = ()
  def modifyLightmap(lightSky: Float, lightBlock: Float, color: Color): Color = color

	def onEntityLivingUpdate(who: WEntityLiving): Unit = {}
}
