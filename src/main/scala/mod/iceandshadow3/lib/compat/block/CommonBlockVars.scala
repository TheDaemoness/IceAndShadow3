package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.block.VarBlockAxis
import mod.iceandshadow3.lib.compat.block.impl.BVarBlockExisting
import mod.iceandshadow3.lib.util.EAb
import net.minecraft.block.SlabBlock
import net.minecraft.state.properties.SlabType

object CommonBlockVars {
	val axis = new VarBlockAxis("axis") {}
	val slab = new BVarBlockExisting(SlabBlock.TYPE, EAb.A) {
		override protected def toUs(in: SlabType) = in match {
			case SlabType.BOTTOM => EAb.A
			case SlabType.TOP => EAb.B
			case SlabType.DOUBLE => EAb.AB
		}
		override protected def toThem(in: EAb) = in.remapTo(SlabType.BOTTOM, SlabType.TOP, SlabType.DOUBLE)
	}
}
