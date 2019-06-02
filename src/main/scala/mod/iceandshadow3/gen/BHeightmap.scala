package mod.iceandshadow3.gen

abstract class BHeightmap(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int) {
	val values = new Array[Float](xWidth*zWidth)
	protected def computeHeight(x: Int, z:Int): Float
	for(xit <- 0 until xWidth) {
		for(zit <- 0 until zWidth) {
			values(xit+zit*xWidth) = computeHeight(xFrom+xit, zFrom+zit)
		}
	}

	/** Takes indices in offsets from xFrom | zFrom.
		*/
	def apply(xOffset:Int, zOffset:Int): Float = values(xOffset+zOffset*xWidth)
}
