package mod.iceandshadow3.compat.block.`type`

import net.minecraft.block.SnowBlock
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks

class BlockTypeSnow(height: Int) extends BBlockType {
	override protected[compat] def state(): BlockState =
		Blocks.SNOW.getDefaultState.`with`(SnowBlock.LAYERS, Integer.valueOf(height+1))
}
object BlockTypeSnow {
	val SNOWS = Array.tabulate[BlockTypeSnow](8){new BlockTypeSnow(_)}
	def fromFloat(height: Double) = SNOWS((height*7).toInt)
}
