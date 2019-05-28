package mod.iceandshadow3.spatial

object SpatialConstants {
	val CHUNK_BITS: Long = 4
	val CHUNK_MULT: Long = 1L << CHUNK_BITS
	val CHUNK_MASK: Long = CHUNK_MULT - 1

	val ZERO: IVec3 = new Vec3Fixed(0, 0, 0)
	// Unit vectors.
	val U: IVec3 = new Vec3Fixed(0, 1, 0)
	val D: IVec3 = new Vec3Fixed(0, -1, 0)
	val S: IVec3 = new Vec3Fixed(0, 0, 1)
	val N: IVec3 = new Vec3Fixed(0, 0, -1)
	val E: IVec3 = new Vec3Fixed(1, 0, 0)
	val W: IVec3 = new Vec3Fixed(-1, 0, 0)
}
