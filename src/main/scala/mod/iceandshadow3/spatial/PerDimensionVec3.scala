package mod.iceandshadow3.spatial

import mod.iceandshadow3.compat.world.CDimensionCoord
import mod.iceandshadow3.data.{DataTreeMapFlexible, IDataTreeRW}

/** A mutable map of DimensionCoords to Vec3Ms.
  */
class PerDimensionVec3 extends IDataTreeRW[DataTreeMapFlexible] {
  private val mapping = new DataTreeMapFlexible(_ => Some(new Vec3Mutable(SpatialConstants.ZERO)))

  def set(which: CDimensionCoord, where: IVec3): Unit =
    mapping.add(which.getId, where.asMutable)

  def get(which: CDimensionCoord): Option[Vec3Mutable] =
    mapping.getAndCast[Vec3Mutable](which.getId)

  override def exposeDataTree() = mapping

  override def fromDataTree(tree: DataTreeMapFlexible) = mapping.fromDataTree(tree)
}
