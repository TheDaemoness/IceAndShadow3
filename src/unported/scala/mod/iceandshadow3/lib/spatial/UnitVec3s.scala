package mod.iceandshadow3.lib.spatial

object UnitVec3s {
	val ZERO: IVec3 = new Vec3Fixed(0, 0, 0)
	// Unit vectors.
	val U: IVec3 = new Vec3Fixed(0, 1, 0)
	val D: IVec3 = new Vec3Fixed(0, -1, 0)
	val S: IVec3 = new Vec3Fixed(0, 0, 1)
	val N: IVec3 = new Vec3Fixed(0, 0, -1)
	val E: IVec3 = new Vec3Fixed(1, 0, 0)
	val W: IVec3 = new Vec3Fixed(-1, 0, 0)
	// Block unit vectors.
	val BU: IVec3 = new Vec3Fixed(0, IVec3.SUB_MULT.toInt, 0)
	val BD: IVec3 = new Vec3Fixed(0, -IVec3.SUB_MULT.toInt, 0)
	val BS: IVec3 = new Vec3Fixed(0, 0, IVec3.SUB_MULT)
	val BN: IVec3 = new Vec3Fixed(0, 0, -IVec3.SUB_MULT)
	val BE: IVec3 = new Vec3Fixed(IVec3.SUB_MULT, 0, 0)
	val BW: IVec3 = new Vec3Fixed(-IVec3.SUB_MULT, 0, 0)
}
