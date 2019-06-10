package mod.iceandshadow3.compat.block.`type`

import javax.annotation.Nullable
import mod.iceandshadow3.basics.BLogicBlock
import mod.iceandshadow3.compat.block.BinderBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

/**The missing CBlockState equivalent.
	*/
class BlockTypeSimple(@Nullable bs: BlockState) extends BBlockType {
	def this(bl: Block) = this(bl.getDefaultState)
	def this(bl: BLogicBlock, variant: Int) = this(BinderBlock(bl)(variant)._1.getDefaultState)
	def this(name: String) = this(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name)))

	override protected[compat] def state(): BlockState =
		if(bs == null) Blocks.AIR.getDefaultState else bs
}
object BlockTypeSimple {
	val BEDROCK = new BlockTypeSimple(Blocks.BEDROCK.getDefaultState)
	val AIR = new BlockTypeSimple(Blocks.AIR.getDefaultState)
}
