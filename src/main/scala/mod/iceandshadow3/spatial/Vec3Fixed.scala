package mod.iceandshadow3.spatial

class Vec3Fixed (
	x: Long,
	y: Int,
	z: Long
)
	extends IVec3
{
	def this(from: IVec3) = this(from.xRaw, from.yRaw, from.zRaw)
	def xRaw: Long = x
	def yRaw: Int = y
	def zRaw: Long = z
	override def asFixed: Vec3Fixed = this
}
