package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.base.ProviderLogic
import mod.iceandshadow3.lib.block.BlockFn
import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.compat.block.impl.{VarBlock, BinderBlock, BinderBlockVar}
import mod.iceandshadow3.lib.compat.world.WSound
import net.minecraft.block.{Block, BlockState}
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

class WBlockState(protected var bs: BlockState)
extends WBlockType
with TBlockStateSource {
	override protected[compat] def asBlock() = exposeBS().getBlock

	def +[T](variable: VarBlock[T], value: T): WBlockState =
		new WBlockState(BinderBlockVar.get(variable).addTo(exposeBS(), value))
	def ?[T](variable: VarBlock[T], pred: T => Boolean): Boolean = {
		val bs = exposeBS()
		val wip = BinderBlockVar.get(variable)
		if(wip.isIn(bs)) pred(wip.in(bs)) else false
	}

	def isComplex = exposeBS().hasTileEntity

	def this(bl: Block) = this(bl.getDefaultState)
	def this(bl: BLogicBlock) = this(BinderBlock(bl)._1.getDefaultState)
	def this(name: String) = this(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name)))

	protected[compat] def exposeBS(): BlockState = bs
	def apply[T](which: VarBlock[T]): Option[T] = {
		val bs = exposeBS()
		val wip = BinderBlockVar.get(which)
		if(wip.isIn(bs)) Some(wip.in(bs)) else None
	}

	@throws[IllegalArgumentException]
	def get[T](which: VarBlock[T]): T = {
		val bs = exposeBS()
		val wip = BinderBlockVar.get(which)
		wip.in(bs)
	}

	override def getLogic = exposeBS().getBlock match {
		case lp: ProviderLogic.Block => lp.getLogic
		case _ => null
	}

	def typeThis = this

	def soundVolume = exposeBS().getSoundType.volume
	def soundPitch = exposeBS().getSoundType.pitch
	def soundDig = WSound(exposeBS().getSoundType.getHitSound)
	def soundBreak = WSound(exposeBS().getSoundType.getBreakSound)
	def soundPlace = WSound(exposeBS().getSoundType.getPlaceSound)

	override def asWBlockState = this
	implicit override def asBlockFn: BlockFn = (x: Int, y: Int, z: Int, in: WBlockState) => WBlockState.this
}
