package mod.iceandshadow3.lib.compat.block.`type`

import javax.annotation.Nullable
import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.compat.block.impl.{BinderBlock, BinderBlockVar}
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

/**The missing WBlockState equivalent.
	*/
class BlockType(@Nullable bs: BlockState) extends BBlockType {
	def this(bl: Block) = this(bl.getDefaultState)
	def this(bl: BLogicBlock, variant: Int) = this(BinderBlock(bl)(variant)._1.getDefaultState)
	def this(name: String) = this(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name)))

	override protected[compat] def state(): BlockState =
		if(bs == null) Blocks.AIR.getDefaultState else bs
}
object BlockType {
	val BEDROCK = new BlockType(Blocks.BEDROCK.getDefaultState)
	val AIR = new BlockType(Blocks.AIR.getDefaultState)
}
