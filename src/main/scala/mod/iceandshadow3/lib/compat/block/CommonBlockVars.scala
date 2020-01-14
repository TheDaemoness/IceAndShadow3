package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.compat.block.impl.VarBlockExisting
import mod.iceandshadow3.lib.spatial.{EAxis, EFacing}
import mod.iceandshadow3.lib.util.EAb
import net.minecraft.block.SlabBlock
import net.minecraft.state.properties.{BlockStateProperties, SlabType}
import net.minecraft.util.Direction

object CommonBlockVars {
	val axis = new VarBlockExisting(BlockStateProperties.AXIS, EAxis.DOWN_UP) {
		override protected def toUs(in: Direction.Axis) = EAxis.fromVanilla(in)
		override protected def toThem(in: EAxis) = in.toVanilla
	}
	val facing = new VarBlockExisting(BlockStateProperties.FACING, EFacing.UP) {
		override protected def toUs(in: Direction) = EFacing.fromVanilla(in)
		override protected def toThem(in: EFacing) = in.toVanilla
	}
	val slab = new VarBlockExisting(SlabBlock.TYPE, EAb.A) {
		override protected def toUs(in: SlabType) = in match {
			case SlabType.BOTTOM => EAb.A
			case SlabType.TOP => EAb.B
			case SlabType.DOUBLE => EAb.AB
		}
		override protected def toThem(in: EAb) = in.remapTo(SlabType.BOTTOM, SlabType.TOP, SlabType.DOUBLE)
	}
}
