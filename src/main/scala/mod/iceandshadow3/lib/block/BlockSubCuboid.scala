package mod.iceandshadow3.lib.block

import mod.iceandshadow3.lib.spatial.{EAxis, EFacing}

case class BlockSubCuboid private(
	xMin: Float, yMin: Float, zMin: Float, xWidth: Float, yWidth: Float, zWidth: Float
) {
	def apply(face: EFacing): Float = face.axis match {
		case EAxis.DOWN_UP =>     if(face.positive) yMin + yWidth else yMin
		case EAxis.WEST_EAST =>   if(face.positive) xMin + xWidth else xMin
		case EAxis.NORTH_SOUTH => if(face.positive) zMin + zWidth else zMin
		//Else explode.
	}
	def rotated(vec: EFacing) = BlockSubCuboid.points(
		apply(EFacing.WEST.rotatedCw(vec)), apply(EFacing.DOWN.rotatedCw(vec)), apply(EFacing.NORTH.rotatedCw(vec)),
		apply(EFacing.EAST.rotatedCw(vec)), apply(EFacing.UP.rotatedCw(vec)),   apply(EFacing.SOUTH.rotatedCw(vec))
	)
}
object BlockSubCuboid {
	def points(xA: Float, yA: Float, zA: Float, xB: Float, yB: Float, zB: Float) = new BlockSubCuboid(
		Math.min(xA, xB),
		Math.min(yA, yB),
		Math.min(zA, zB),
		Math.abs(xB-xA),
		Math.abs(yB-yA),
		Math.abs(zB-zA)
	)
	def apply(
		height: Float = 16, baseHeight: Float = 0,
		width: Float = 16, biasDepth: Float = 0,
		skewX: Float = 0, skewZ: Float = 0
	): BlockSubCuboid = {
		val widthZ = width+biasDepth
		new BlockSubCuboid(
			8f + skewX - width/2, baseHeight, 8f + skewZ - widthZ/2,
			width, height, widthZ
		)
	}
}
