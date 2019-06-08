package mod.iceandshadow3.basics.block

import mod.iceandshadow3.compat.block.ESideType

case class BlockSides(
	top: ESideType, bottom: ESideType,
	front: ESideType, back: ESideType,
	left: ESideType, right: ESideType
) {
	val isFullCube = {
		top == ESideType.FULL && bottom == ESideType.FULL &&
		front == ESideType.FULL && back == ESideType.FULL &&
		left == ESideType.FULL && right == ESideType.FULL
	}
}
object BlockSides {
	val FULL = BlockSides(
		ESideType.FULL, ESideType.FULL,
		ESideType.FULL, ESideType.FULL,
		ESideType.FULL, ESideType.FULL
	)
	val UNEVEN = BlockSides(
		ESideType.OTHER, ESideType.OTHER,
		ESideType.OTHER, ESideType.OTHER,
		ESideType.OTHER, ESideType.OTHER
	)
}
