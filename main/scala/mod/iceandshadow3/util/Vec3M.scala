package mod.iceandshadow3.util

class Vec3M(xO: Long, yO: Int, zO: Long) extends Vec3(xO, yO, zO) {
	def add(x: Int, y: Int, z: Int): Vec3M = {
		this.x += x << Vec3.SUB_BITS
		this.y += y.toShort << Vec3.SUB_BITS
		this.z += z << Vec3.SUB_BITS
		this
	}

	def add(x: Double, y: Double, z: Double): Vec3M = {
		this.x += Vec3.toLong(x)
		this.y += Vec3.toLong(y).toInt
		this.z += Vec3.toLong(z)
		this
	}

	def add(b: Vec3): Vec3M = {
		x += b.xRaw
		y += b.yRaw
		z += b.zRaw
		this
	}

	def sub(b: Vec3): Vec3M = {
		x -= b.xRaw
		y -= b.yRaw
		z -= b.zRaw
		this
	}

	def mult(multiplier: Double): Vec3M = {
		//TODO: I bet there are neater ways of doing this.
		x = (x.toDouble*multiplier).toLong
		y = (y.toDouble*multiplier).toInt
		z = (z.toDouble*multiplier).toLong
		this
	}

/// Converts the vector to its normal equivalent (magnitude = 1).
	def norm(): Vec3M = mult(1 / mag())

	def inv(): Vec3M = mult(-1)

}