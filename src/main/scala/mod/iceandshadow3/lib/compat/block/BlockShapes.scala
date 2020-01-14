package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.block.BlockShape
import net.minecraft.block.Block
import net.minecraft.util.math.shapes.{VoxelShape, VoxelShapes}

sealed abstract class BlockShapes {
	protected[block] def apply(view: WBlockView): VoxelShape
}
object BlockShapes {
	private def convert(what: BlockShape): VoxelShape = {
		var result: VoxelShape = null
		for (box <- what.boxes) {
			val mcbox = Block.makeCuboidShape(
				box.xMin, box.yMin, box.zMin,
				box.xMin + box.xWidth,
				box.yMin + box.yWidth,
				box.zMin + box.zWidth
			)
			result = if(result == null) mcbox else VoxelShapes.or(result, mcbox)
		}
		result
	}

	val empty = new BlockShapes {
		override protected[block] def apply(view: WBlockView) = VoxelShapes.empty()
	}
	val full = new BlockShapes {
		override protected[block] def apply(view: WBlockView) = VoxelShapes.fullCube()
	}
	implicit def apply(what: BlockShape): BlockShapes = new BlockShapes {
		private val shape = convert(what)
		override protected[block] def apply(view: WBlockView) = shape
	}
	def apply(mapper: WBlockView => Int, default: BlockShape, shapes: BlockShape*) = new BlockShapes {
		private val shapeArr = shapes.map(convert).toArray
		private val shapeDefault = convert(default)
		override protected[block] def apply(view: WBlockView) =
			shapeArr.applyOrElse(mapper(view), (_: Int) => shapeDefault)
	}
}
