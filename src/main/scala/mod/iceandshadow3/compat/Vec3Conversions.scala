package mod.iceandshadow3.compat

import mod.iceandshadow3.util.Vec3
import net.minecraft.util.math.BlockPos

object Vec3Conversions {
  implicit def toBlockPos(vec3: Vec3): BlockPos = new BlockPos(vec3.xBlock, vec3.yBlock, vec3.zBlock)
  implicit def fromBlockPos(bp: BlockPos): Vec3 = new Vec3(bp.getX, bp.getY, bp.getZ, Vec3.SUB_BITS)
}
