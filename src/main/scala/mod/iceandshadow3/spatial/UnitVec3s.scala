package mod.iceandshadow3.spatial

object UnitVec3s {
	val ZERO: IVec3 = new Vec3Fixed(0, 0, 0)
	// Unit vectors.
	val U: IVec3 = new Vec3Fixed(0, 1, 0)
	val D: IVec3 = new Vec3Fixed(0, -1, 0)
	val S: IVec3 = new Vec3Fixed(0, 0, 1)
	val N: IVec3 = new Vec3Fixed(0, 0, -1)
	val E: IVec3 = new Vec3Fixed(1, 0, 0)
	val W: IVec3 = new Vec3Fixed(-1, 0, 0)
}
