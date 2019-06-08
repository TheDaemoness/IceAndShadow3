package mod.iceandshadow3.world.gaia

import mod.iceandshadow3.basics.block.IMateria

object MatNavistraBedrock extends BMateriaNavistra {
	override def getName = "navistra_bedrock"
	override def getBaseHardness = IMateria.indestructibleByMining
}
