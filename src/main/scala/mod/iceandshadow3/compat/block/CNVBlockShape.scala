package mod.iceandshadow3.compat.block

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.BLogicBlock
import mod.iceandshadow3.basics.block.BlockShape
import net.minecraft.block.Block
import net.minecraft.util.math.shapes.{VoxelShape, VoxelShapes}

import scala.language.implicitConversions

object CNVBlockShape {
	implicit def toVoxelShape(logic: BLogicBlock): VoxelShape = {
		val what = logic.shape
		if(what == BlockShape.FULL_CUBE) VoxelShapes.fullCube()
		else if(what == BlockShape.DECO) VoxelShapes.empty()
		else if(what != null) {
			IaS3.logger().debug("Reached shapemaker: "+what.boxes.size)
			var result: VoxelShape = null
			for (box <- what.boxes) {
				val mcbox = Block.makeCuboidShape(
					8 - box.width / 2, box.baseHeight, 8 - box.width / 2,
					8 + box.width / 2, box.baseHeight + box.height, 8 + box.width / 2
				)
				result = if(result == null) mcbox else VoxelShapes.or(result, mcbox)
			}
			result
		} else {
			IaS3.bug(logic, "BlockShape returned is null")
			VoxelShapes.empty()
		}
	}
}
