package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.compat.block.impl.VarBlockExisting
import mod.iceandshadow3.lib.spatial.EAxis
import mod.iceandshadow3.lib.util.EAb
import net.minecraft.block.SlabBlock
import net.minecraft.state.properties.{BlockStateProperties, SlabType}
import net.minecraft.util.Direction

object CommonBlockVars {
	val axis = new VarBlockExisting(BlockStateProperties.AXIS, EAxis.DOWN_UP) {
		override protected def toUs(in: Direction.Axis) = in match {
			case Direction.Axis.X => EAxis.WEST_EAST
			case Direction.Axis.Y => EAxis.DOWN_UP
			case Direction.Axis.Z => EAxis.NORTH_SOUTH
		}

		override protected def toThem(in: EAxis) = in match {
			case EAxis.WEST_EAST => Direction.Axis.X
			case EAxis.DOWN_UP => Direction.Axis.Y
			case EAxis.NORTH_SOUTH => Direction.Axis.Z
		}
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
