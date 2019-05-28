package mod.iceandshadow3.compat.block

import mod.iceandshadow3.basics.damage.Damage
import mod.iceandshadow3.compat.Vec3Conversions
import mod.iceandshadow3.compat.entity.TEffectSource
import mod.iceandshadow3.compat.world.TCWorldPlace
import mod.iceandshadow3.spatial.{IPositional, IVec3}
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.{IBlockReader, World}
import net.minecraft.world.chunk.Chunk

sealed abstract class Impl protected(val pos: BlockPos) {
	def exposeBS(): IBlockState //Where to begin in the world?
	def exposeIBA(): IBlockReader
	def exposeWorld(): World
}
object CRefBlock {
	private class ImplChunk(chunk: Chunk, position: BlockPos)
			extends Impl(position) {
		def exposeBS(): IBlockState = chunk.getBlockState(pos)
		def exposeIBA(): IBlockReader = chunk.getWorld
		def exposeWorld(): World = chunk.getWorld
	}

	private class ImplStupid(val notaworld: IBlockReader, position: BlockPos)
			extends Impl(position) {
		val maybeaworld: World = notaworld match {
			case world: World => world
			case _ => null
		}

		def exposeBS(): IBlockState = notaworld.getBlockState(pos)
		def exposeIBA(): IBlockReader = notaworld
		def exposeWorld(): World = maybeaworld
	}
}

class CRefBlock(private val impl: Impl) extends TCWorldPlace with TEffectSource with IPositional {

	override protected[compat] def exposeWorld(): World = impl.exposeWorld()

	def this(w: World, v: IVec3) {
		this(new CRefBlock.ImplChunk(
		w.getChunk(v.xChunk, v.zChunk),
		new BlockPos(v.xBlock.toInt,
								 Math.min(v.yBlock, 255).toShort,
								 v.zBlock.toInt)))
	}

	def this(w: World, p: BlockPos) = {
		this(new CRefBlock.ImplChunk(w.getChunk(p.getX >> 4, p.getZ >> 4), p))
	}

	def this(notaworld: IBlockReader, bp: BlockPos) = {
		this(new CRefBlock.ImplStupid(notaworld, bp))
	}

	def getHardness: Float = {
		val w: World = impl.exposeWorld()
		if (w == null) java.lang.Float.NaN
		impl.exposeBS().getBlockHardness(w, impl.pos)
	}

	def getOpacity: Int = impl.exposeBS().getOpacity(impl.exposeIBA(), impl.pos)

	def getLuma: Int = impl.exposeBS().getLightValue(impl.exposeWorld(), impl.pos)

	override def getNameTextComponent: ITextComponent =
		impl.exposeBS().getBlock.getNameTextComponent

	override def getAttack: Damage = null
	
	override def position = Vec3Conversions.fromBlockPos(impl.pos)
}
