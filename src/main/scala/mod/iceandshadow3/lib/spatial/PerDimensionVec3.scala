package mod.iceandshadow3.lib.spatial

import mod.iceandshadow3.lib.compat.world.WDimensionCoord
import mod.iceandshadow3.lib.data.{DataTreeMapFlexible, IDataTreeRW}

/** A mutable map of DimensionCoords to Vec3Ms.
  */
class PerDimensionVec3 extends IDataTreeRW[DataTreeMapFlexible] {
  private val mapping = new DataTreeMapFlexible(_ => Some(new Vec3Mutable(UnitVec3s.ZERO)))

  def update(which: WDimensionCoord, where: IVec3): PerDimensionVec3 = {
    mapping.add(which.getId, where.asMutable)
    this
  }

  def get(which: WDimensionCoord): Option[Vec3Mutable] =
    mapping.getAndCast[Vec3Mutable](which.getId)

  override def exposeDataTree() = mapping

  override def fromDataTree(tree: DataTreeMapFlexible) = mapping.fromDataTree(tree)
}
