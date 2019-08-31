package mod.iceandshadow3.lib.compat.block.`type`

import net.minecraft.block.{BlockState, Blocks, SnowBlock}

class BlockTypeSnow(height: Int) extends TBlockStateSource {
	override protected[compat] def exposeBS(): BlockState =
		Blocks.SNOW.getDefaultState.`with`(SnowBlock.LAYERS, Integer.valueOf(height+1))
}
object BlockTypeSnow {
	val SNOWS = Array.tabulate[BlockTypeSnow](8){new BlockTypeSnow(_)}
	def fromFloat(height: Double) = SNOWS((height*7).toInt)
}
