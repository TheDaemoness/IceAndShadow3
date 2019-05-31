package mod.iceandshadow3.compat.block

import javax.annotation.Nullable
import mod.iceandshadow3.basics.BLogicBlock
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

/**The missing CBlockState equivalent.
	*/
class BlockTypeSimple(@Nullable bs: IBlockState) extends BBlockType {
	def this(bl: Block) = this(bl.getDefaultState)
	def this(bl: BLogicBlock, variant: Int) = this(bl.getSecrets[Block].get(variant).getDefaultState)
	def this(name: String) = this(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name)))

	override protected[compat] def state(): IBlockState =
		if(bs == null) Blocks.AIR.getDefaultState else bs
}
object BlockTypeSimple {
	val AIR = new BlockTypeSimple(Blocks.AIR.getDefaultState)
}
