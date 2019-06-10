package mod.iceandshadow3.compat.block.`type`

import net.minecraft.block.BlockSnowLayer
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks

class BlockTypeSnow(height: Int) extends BBlockType {
	override protected[compat] def state(): IBlockState =
		Blocks.SNOW.getDefaultState.`with`(BlockSnowLayer.LAYERS, Integer.valueOf(height+1))
}
object BlockTypeSnow {
	val SNOWS = Array.tabulate[BlockTypeSnow](8){new BlockTypeSnow(_)}
	def fromFloat(height: Double) = SNOWS((height*7).toInt)
}
