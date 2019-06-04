package mod.iceandshadow3.gen

import mod.iceandshadow3.spatial.{RandomXYZ, RandomXZ, TriadXYZ}

object Cellmaker {
	class Result {
		var distanceClosest = Double.PositiveInfinity
		var distanceSecond = Double.PositiveInfinity
		var cellClosest = TriadXYZ(0, 0, 0)
		var cellSecond = TriadXYZ(0, 0, 0)
		def makeRandomXZ(seed: Long, mod: Int) = new RandomXZ(seed, mod, cellClosest.x, cellClosest.z)
		def makeRandomXYZ(seed: Long, mod: Int) = new RandomXYZ(seed, mod, cellClosest.x, cellClosest.y, cellClosest.z)
		def update(distance: Double, x: Int, y: Int, z: Int): Unit = {
			if (distance < distanceSecond) {
				if (distance < distanceClosest) {
					distanceSecond = distanceClosest
					distanceClosest = distance
					cellSecond = cellClosest
					cellClosest.set(x, y, z)
				} else {
					distanceSecond = distance
					cellSecond.set(x, y, z)
				}
			}
		}
	}
	def rescale(in: Int, scale: Int): Int = {
		val inMod = in+scale/2
		inMod / scale - (if (inMod < 0) 1 else 0)
	}
	def relativeToCellEdge(in: Int, scale: Int, cell: Int): Int = {
		val cellfirst = cell*scale - scale/2
		in - cellfirst
	}
	def distance(result: Cellmaker.Result) =
		(2*result.distanceClosest)/(result.distanceClosest+result.distanceSecond)
}
