package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.base.ILogicBlockProvider
import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.compat.block.impl.{BBlockVar, BBlockVarNew, BinderBlock, BinderBlockVar}
import mod.iceandshadow3.lib.compat.world.WSound
import net.minecraft.block.{Block, BlockState}
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

class WBlockState(protected var bs: BlockState)
extends ILogicBlockProvider with TBlockStateSource {
	def +[T](variable: BBlockVarNew[T], value: T): WBlockState =
		new WBlockState(BinderBlockVar.get(variable).addTo(exposeBS(), value))

	def this(bl: Block) = this(bl.getDefaultState)
	def this(bl: BLogicBlock, variant: Int) = this(BinderBlock(bl)(variant)._1.getDefaultState)
	def this(name: String) = this(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name)))

	protected[compat] def exposeBS(): BlockState = bs
	def apply[T](which: BBlockVar[T]): Option[T] = {
		val bs = exposeBS()
		val wip = BinderBlockVar.get(which)
		if(wip.isIn(bs)) Some(wip.in(bs)) else None
	}

	override def getLogicPair = exposeBS().getBlock match {
		case lp: ILogicBlockProvider => lp.getLogicPair
		case _ => null
	}

	def typeDefault = new WBlockState(exposeBS().getBlock.getDefaultState)
	def typeThis = this

	def soundVolume = exposeBS().getSoundType.volume
	def soundPitch = exposeBS().getSoundType.pitch
	def soundDig = WSound(exposeBS().getSoundType.getHitSound)
	def soundBreak = WSound(exposeBS().getSoundType.getBreakSound)
	def soundPlace = WSound(exposeBS().getSoundType.getPlaceSound)
}
