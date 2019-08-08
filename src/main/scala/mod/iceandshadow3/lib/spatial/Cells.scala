package mod.iceandshadow3.lib.spatial

object Cells {
	class Result {
		var distanceClosest = Double.PositiveInfinity
		var distanceSecond = Double.PositiveInfinity
		val cellClosest = TriadXYZ(0, 0, 0)
		val cellSecond = TriadXYZ(0, 0, 0)
		def makeRandomXZ(seed: Long, mod: Int) = new RandomXZ(seed, mod, cellClosest.x, cellClosest.z)
		def makeRandomXYZ(seed: Long, mod: Int) = new RandomXYZ(seed, mod, cellClosest.x, cellClosest.y, cellClosest.z)
		def update(distance: Double, xCell: Int, yCell: Int, zCell: Int): Unit = {
			if (distance < distanceSecond) {
				if (distance < distanceClosest) {
					distanceSecond = distanceClosest
					distanceClosest = distance
					cellSecond.set(cellClosest)
					cellClosest.set(xCell, yCell, zCell)
				} else {
					distanceSecond = distance
					cellSecond.set(xCell, yCell, zCell)
				}
			}
		}
	}
	def rescale(in: Int, scale: Int): Int = {
		val inMod = in+(scale>>1)
		inMod / scale - (if (inMod < 0) 1 else 0)
	}
	def cellEdge(scale: Int, cell: Int) = cell*scale - (scale>>1)
	def relativeToCellEdge(in: Int, scale: Int, cell: Int): Int = {
		in - cellEdge(scale, cell)
	}
	def distance(result: Cells.Result) =
		(2*result.distanceClosest)/(result.distanceClosest+result.distanceSecond)
}
