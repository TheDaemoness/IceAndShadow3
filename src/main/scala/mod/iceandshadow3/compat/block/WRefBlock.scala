package mod.iceandshadow3.compat.block

import mod.iceandshadow3.compat.{CNVVec3, block}
import mod.iceandshadow3.compat.entity.TEffectSource
import mod.iceandshadow3.compat.world.TWWorldPlace
import mod.iceandshadow3.damage.Attack
import mod.iceandshadow3.spatial.{IPositional, IVec3}
import net.minecraft.block.state.{BlockFaceShape, IBlockState}
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.{IBlockReader, World}
import net.minecraft.world.chunk.Chunk

//TODO: This needs to be split into WBlockView and WBlockRef before any big accidents happen.

object WRefBlock {
	sealed abstract class Impl protected(val pos: BlockPos) {
		def exposeBS(): IBlockState //Where to begin in the world?
		def exposeIBR(): IBlockReader
		def exposeWorld(): World
	}
	private class ImplChunk(chunk: Chunk, position: BlockPos)
			extends Impl(position) {
		def exposeBS(): IBlockState = chunk.getBlockState(pos)
		def exposeIBR(): IBlockReader = chunk.getWorld
		def exposeWorld(): World = chunk.getWorld
	}

	private class ImplStupid(notaworld: IBlockReader, position: BlockPos)
			extends Impl(position) {
		val maybeaworld: World = notaworld match {
			case world: World => world
			case _ => null
		}

		def exposeBS(): IBlockState = notaworld.getBlockState(pos)
		def exposeIBR(): IBlockReader = notaworld
		def exposeWorld(): World = maybeaworld
	}

	private class ImplTransient(world: World, position: BlockPos, state: IBlockState)
		extends Impl(position) {
		def exposeBS(): IBlockState = state
		def exposeIBR(): IBlockReader = world
		def exposeWorld(): World = world
	}
}
import WRefBlock.Impl
class WRefBlock(private val impl: Impl) extends TWWorldPlace with TEffectSource with IPositional {

	override protected[compat] def exposeWorld(): World = impl.exposeWorld()

	def this(w: World, v: IVec3) {
		this(new WRefBlock.ImplChunk(
		w.getChunk(v.xChunk, v.zChunk),
		new BlockPos(v.xBlock.toInt,
								 Math.min(v.yBlock, 255).toShort,
								 v.zBlock.toInt)))
	}

	def this(w: World, p: BlockPos) = {
		this(new WRefBlock.ImplChunk(w.getChunk(p.getX >> 4, p.getZ >> 4), p))
	}

	def this(notaworld: IBlockReader, bp: BlockPos) = {
		this(new block.WRefBlock.ImplStupid(notaworld, bp))
	}

	def this(notaworld: World, bp: BlockPos, bs: IBlockState) = {
		this(new WRefBlock.ImplTransient(notaworld, bp, bs))
	}

	def getHardness: Float = {
		val w: World = impl.exposeWorld()
		if (w == null) java.lang.Float.NaN
		impl.exposeBS().getBlockHardness(w, impl.pos)
	}

	def getOpacity: Int = impl.exposeBS().getOpacity(impl.exposeIBR(), impl.pos)

	def getLuma: Int = impl.exposeBS().getLightValue(impl.exposeWorld(), impl.pos)

	override def getNameTextComponent: ITextComponent =
		impl.exposeBS().getBlock.getNameTextComponent

	override def getAttack: Attack = null

	override def position = CNVVec3.fromBlockPos(impl.pos)

	def atOffset(x: Int, y: Int, z: Int): WRefBlock = {
		val maybeworld = impl.exposeWorld()
		val pos = impl.pos.add(x, y, z)
		if(maybeworld == null) new WRefBlock(impl.exposeIBR(), pos) else new WRefBlock(maybeworld, pos)
	}

	private[block] def isSideSolid(facing: EnumFacing) =
		impl.exposeBS().getBlockFaceShape(impl.exposeIBR(), impl.pos, facing) == BlockFaceShape.SOLID
	def isFullCube = impl.exposeBS().isFullCube

	def hasWorld: Boolean = impl.exposeWorld() != null

	def break(drops: Boolean): Unit = {
		//TODO: Block type on breakage.
		if(isServerSide) impl.exposeWorld().destroyBlock(impl.pos, drops)
	}
}
