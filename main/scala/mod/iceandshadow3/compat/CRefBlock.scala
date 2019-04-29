package mod.iceandshadow3.compat

import net.minecraft.block.material.EnumPushReaction
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk

sealed abstract class Impl protected(val pos: BlockPos) {
	def getBS(): IBlockState
	def getIBA(): IBlockReader
	def getWorld(): World
}
object CRefBlock {
	private class ImplChunk(val chunk: Chunk, position: BlockPos)
			extends Impl(position) {
		def getBS(): IBlockState = chunk.getBlockState(pos)
		def getIBA(): IBlockReader = chunk.getWorld
		def getWorld(): World = chunk.getWorld
	}

	private class ImplStupid(val notaworld: IBlockReader, position: BlockPos)
			extends Impl(position) {
		val maybeaworld: World =
			if ((notaworld.isInstanceOf[World])) notaworld.asInstanceOf[World] else null

		def getBS(): IBlockState = notaworld.getBlockState(pos)
		def getIBA(): IBlockReader = notaworld
		def getWorld(): World = maybeaworld
	}
}

class CRefBlock(private val impl: Impl) extends TCRefWorld with IEffectSource {
	
	protected override def getWorld(): World = impl.getWorld
	
	def this(w: World, v: Vec3) {
		this(new CRefBlock.ImplChunk(
		w.getChunk(v.xChunk(), v.zChunk()),
		new BlockPos(v.xBlock().toInt,
								 Math.min(v.yBlock(), 255).toShort,
								 v.zBlock().toInt)))
	}

	def this(w: World, p: BlockPos) = {
		this(new CRefBlock.ImplChunk(w.getChunk(p.getX >> 4, p.getZ >> 4), p))
	}

	def this(notaworld: IBlockReader, bp: BlockPos) = {
		this(new CRefBlock.ImplStupid(notaworld, bp))
	}

	def getHardness(): Float = {
		val w: World = impl.getWorld
		if (w == null) java.lang.Float.NaN
		impl.getBS.getBlockHardness(w, impl.pos)
	}

	def getOpacity(): Int = impl.getBS.getOpacity(impl.getIBA, impl.pos)

	def getLuma(): Int = impl.getBS.getLightValue(impl.getWorld, impl.pos)
	
	override def getNameTextComponent(): ITextComponent =
		impl.getBS.getBlock.getNameTextComponent

	override def getAttack(): mod.iceandshadow3.basics.Damage = null

}
