package mod.iceandshadow3.compat.world

import mod.iceandshadow3.data.{DataTreeMapFlexible, IDataTreeRW}
import mod.iceandshadow3.util.{Vec3, Vec3M}

/** A mutable map of DimensionCoords to Vec3Ms.
  */
class PerDimensionVec3 extends IDataTreeRW[DataTreeMapFlexible] {
  private val mapping = new DataTreeMapFlexible(_ => Some(new Vec3M(Vec3.ZERO)))

  def set(which: CDimensionCoord, where: Vec3): Unit =
    mapping.add(which.getId, where.getMutable)

  def get(which: CDimensionCoord): Option[Vec3M] =
    mapping.getAndCast[Vec3M](which.getId)

  override def exposeDataTree() = mapping

  override def fromDataTree(tree: DataTreeMapFlexible) = mapping.fromDataTree(tree)
}
