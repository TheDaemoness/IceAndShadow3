package mod.iceandshadow3.lib.spatial

trait IPositionalCoarse {
	def posCoarse: IPosBlock
}
trait IPositionalFine extends IPositionalCoarse {
	def posFine: IVec3
	def posCoarse: IPosBlock = posFine
}
