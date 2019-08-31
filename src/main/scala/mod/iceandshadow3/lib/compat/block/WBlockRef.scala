package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.compat.block.`type`.{CommonBlockTypes, TBlockStateSource}
import mod.iceandshadow3.lib.compat.util.CNVCompat
import mod.iceandshadow3.lib.compat.world.{TWWorld, TWWorldPlace, WSound}
import mod.iceandshadow3.lib.spatial.IPosBlock
import net.minecraft.block.BlockState
import net.minecraft.entity.item.FallingBlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.{IWorld, World}

class WBlockRef(chunk: IChunk, pos: BlockPos, bs: BlockState) extends WBlockView(chunk.getWorldForge, pos, bs)
	with TWWorldPlace
{
	override protected def acquireBS() = chunk.getBlockState(pos)
	override protected[compat] def exposeWorld(): IWorld = chunk.getWorldForge

	final override def promote(wr: TWWorld): WBlockRef = this

	def this(w: IWorld, v: IPosBlock) {
		this(w.getChunk(v.xChunk, v.zChunk), CNVCompat.toBlockPos(v), null)
	}

	def this(w: IWorld, p: BlockPos) = {
		this(w.getChunk(p.getX >> 4, p.getZ >> 4), p, null)
	}

	def this(w: IWorld, p: BlockPos, bs: BlockState) = {
		this(w.getChunk(p.getX >> 4, p.getZ >> 4), p, bs)
	}

	override def atOffset(x: Int, y: Int, z: Int): WBlockRef =
		new WBlockRef(exposeWorld(), pos.add(x, y, z))

	def set(what: TBlockStateSource): Unit = {
		if(isServerSide) {
			exposeWorld().setBlockState(pos, what.exposeBS(), 3)
			refresh()
		}
	}
	def place(what: TBlockStateSource): Boolean = {
		val retval = what.exposeBS().isValidPosition(exposeWorld(), pos)
		if(retval) set(what)
		retval
	}
	def break(drops: Boolean): Unit = {
		//TODO: Block type on breakage.
		if(isServerSide) {
			exposeWorld().destroyBlock(pos, drops)
			refresh()
		}
	}
	def break(ifSofterThan: Float, drops: Boolean): Boolean = {
		if(getHardness < ifSofterThan) {break(drops); true}
		else false
	}
	def fall(): Boolean = {
		exposeWorld() match {
			case w: World =>
				val fp = this.posFine
				val ent = new FallingBlockEntity(w, fp.xDouble, fp.yDouble, fp.zDouble, exposeBS())
				ent.setOrigin(pos)
				set(CommonBlockTypes.AIR)
				refresh()
				exposeWorld().addEntity(ent)
				return true
			case _ => return false
		}
	}
	def change[T](fn: WBlockState => TBlockStateSource) = set(fn(typeThis))

	def playSound(sound: WSound): Unit = sound.play(this, this.posCoarse, this.soundVolume, this.soundPitch)
}
