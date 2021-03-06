package mod.iceandshadow3.lib.compat.block

import javax.annotation.Nullable
import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.compat.util.{CNVCompat, TLocalized, TWLogical}
import mod.iceandshadow3.lib.compat.world.TWWorld
import mod.iceandshadow3.lib.spatial.{IPosBlock, IPositionalFine}
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

class WBlockView(protected val ibr: IBlockReader, protected val pos: BlockPos, bls: BlockState)
	extends WBlockState(bls)
	with IPositionalFine
	with TLocalized
	with IPosBlock
	with TWLogical[BLogicBlock]
{
	def this(w: IBlockReader, p: BlockPos) = {
		this(w, p, null)
	}
	def this(w: IBlockReader, p: IPosBlock) = {
		this(w, new BlockPos(p.xBlock, p.yBlock, p.zBlock), null)
	}

	def promote(wr: TWWorld): WBlockRef = new WBlockRef(wr.exposeWorld(), pos, exposeBS())

	override protected[compat] final def exposeBS(): BlockState = {if(bs == null) refresh(); bs}
	protected def acquireBS(): BlockState = ibr.getBlockState(pos)
	final def refresh(): Unit = {bs = acquireBS();}

	override def posFine = CNVCompat.fromBlockPos(pos)

	def hardness: Float = exposeBS().getBlockHardness(ibr, pos)
	def opacity: Int = exposeBS().getOpacity(ibr, pos)

	override protected[compat] def getLocalizedName = exposeBS().getBlock.getNameTextComponent

	def atOffset(x: Int, y: Int, z: Int): WBlockView =
		new WBlockView(ibr, pos.add(x, y, z))

	def isPlain = exposeBS().isNormalCube(ibr, pos)

	override protected def exposeCompoundOrNull() = {
		val tileentity = Option(ibr.getTileEntity(pos))
		tileentity.fold[CompoundNBT](null)(tent => {tent.getTileData})
	}

	def isAir: Boolean = exposeBS().isAir(ibr, pos)

	override def yBlock = pos.getY
	override def xBlock = pos.getX
	override def zBlock = pos.getZ

	@Nullable private[compat] def tileEntity: TileEntity = ibr.getTileEntity(pos)
}
