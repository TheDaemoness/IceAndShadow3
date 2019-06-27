package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.util.ILogicBlockProvider
import mod.iceandshadow3.damage.Attack
import mod.iceandshadow3.lib.block.IMateria
import mod.iceandshadow3.lib.compat.util.{CNVCompat, TEffectSource, TWLogical}
import mod.iceandshadow3.spatial.{IPosBlock, IPositionalFine}
import net.minecraft.block.BlockState
import net.minecraft.item.BlockItemUseContext
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

class WBlockView(protected val ibr: IBlockReader, protected val pos: BlockPos, private var bs: BlockState)
	extends IPositionalFine
	with ILogicBlockProvider
	with TEffectSource
	with IPosBlock
	with TWLogical[BLogicBlock]
{
	def this(w: IBlockReader, p: BlockPos) = {
		this(w, p, null)
	}
	def this(w: IBlockReader, p: IPosBlock) = {
		this(w, new BlockPos(p.xBlock, p.yBlock, p.zBlock), null)
	}
	override def registryName: String = exposeBS().getBlock.getRegistryName.toString
	//TODO: Fluids.

	protected[compat] final def exposeBS(): BlockState = {if(bs == null) refresh(); bs}
	protected def acquireBS(): BlockState = ibr.getBlockState(pos)
	final def refresh(): Unit = {bs = acquireBS();}
	override def posFine = CNVCompat.fromBlockPos(pos)

	def getHardness: Float = exposeBS().getBlockHardness(ibr, pos)
	def getOpacity: Int = exposeBS().getOpacity(ibr, pos)

	def isSolid = exposeBS().isSolid

	def isMateria(materia: Class[_ <: IMateria]): Boolean = {
		val lpo = Option(this.getLogicPair)
		lpo.fold(false)({
			_.logic.isOfMateria(materia)
		})
	}

	override protected[compat] def getNameTextComponent = exposeBS().getBlock.getNameTextComponent
	override def getAttack: Attack = null

	def atOffset(x: Int, y: Int, z: Int): WBlockView =
		new WBlockView(ibr, pos.add(x, y, z))

	def isComplex = ibr.getTileEntity(pos) != null

	override def getLogicPair = exposeBS().getBlock match {
		case lp: ILogicBlockProvider => lp.getLogicPair
		case _ => null
	}

	override protected def exposeCompoundOrNull() = {
		val tileentity = Option(ibr.getTileEntity(pos))
		tileentity.fold[CompoundNBT](null)(tent => {tent.getTileData})
	}

	def resistsExousia: Boolean = {
		val lp = getLogicPair
		if(lp == null) getHardness < 0f || getHardness >= 150f
		else lp.logic.resistsExousia(lp.variant)
	}

	def isAir: Boolean = exposeBS().isAir(ibr, pos)

	override def yBlock = pos.getY
	override def xBlock = pos.getX
	override def zBlock = pos.getZ
}
