package mod.iceandshadow3.gen

import scala.reflect.ClassTag

class TerrainMap[T: ClassTag](xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int, compute: (Int,Int) => T) {
	val values = new Array[T](xWidth*zWidth)
	for(xit <- 0 until xWidth) {
		for(zit <- 0 until zWidth) {
			values(xit+zit*xWidth) = compute(xFrom+xit, zFrom+zit)
		}
	}

	def apply(x:Int, z:Int): T = values((x-xFrom)+(z-zFrom)*xWidth)
}
