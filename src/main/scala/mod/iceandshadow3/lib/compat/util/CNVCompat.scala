package mod.iceandshadow3.lib.compat.util

import mod.iceandshadow3.lib.spatial.{CNVSpatial, IPosBlock, IVec3, Vec3Mutable}
import net.minecraft.entity.Entity
import net.minecraft.util.math.{BlockPos, Vec3d}

import scala.language.implicitConversions

object CNVCompat {
	implicit def toBlockPos(vec3: IPosBlock): BlockPos = new BlockPos(vec3.xBlock, vec3.yBlock, vec3.zBlock)
	implicit def fromBlockPos(bp: BlockPos): IVec3 = new IVec3 {
		override def xRaw = IVec3.fromBlockCoord(bp.getX)+IVec3.SUB_MULT/2
		override def yRaw = IVec3.fromBlockCoord(bp.getY).toInt
		override def zRaw = IVec3.fromBlockCoord(bp.getZ)+IVec3.SUB_MULT/2
		override def xBlock = bp.getX
		override def yBlock = bp.getY
		override def zBlock = bp.getZ
	}
	implicit def fromEntity(ent: Entity): IVec3 = CNVSpatial.fromDoubles(ent.posX, ent.posY, ent.posZ)
	implicit def fromVec3d(v3f: Vec3d): IVec3 = new Vec3Mutable (
		IVec3.fromDouble(v3f.x),
		IVec3.fromDouble(v3f.y).toInt,
		IVec3.fromDouble(v3f.z)
	)
}
