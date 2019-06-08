package mod.iceandshadow3.compat.block

import mod.iceandshadow3.util.IteratorConcat
import net.minecraft.util.EnumFacing

import scala.collection.JavaConverters._

sealed abstract class AdjacentBlocks(val central: WRefBlock, protected val whats: (WRefBlock, EnumFacing)*)
	extends Iterable[WRefBlock]
{
	def areSidesSolid = whats.forall(pair => pair._1.isSideSolid(pair._2))
	override def iterator = new IteratorConcat[(WRefBlock, EnumFacing), WRefBlock]({_._1}, whats.iterator.asJava)
}
object AdjacentBlocks {
	protected def up(center: WRefBlock) =
		(center.atOffset(0, 1, 0), EnumFacing.DOWN)
	protected def down(center: WRefBlock) =
		(center.atOffset(0, -1, 0), EnumFacing.UP)
	protected def south(center: WRefBlock) =
		(center.atOffset(0, 0, 1), EnumFacing.NORTH)
	protected def north(center: WRefBlock) =
		(center.atOffset(0, 0, -1), EnumFacing.SOUTH)
	protected def east(center: WRefBlock) =
		(center.atOffset(1, 0, 0), EnumFacing.WEST)
	protected def west(center: WRefBlock) =
		(center.atOffset(-1, 0, 0), EnumFacing.EAST)

	class Above(center: WRefBlock) extends AdjacentBlocks(center,
		up(center)
	)
	class Below(center: WRefBlock) extends AdjacentBlocks(center,
		down(center)
	)
	class Southern(center: WRefBlock) extends AdjacentBlocks(center,
		south(center)
	)
	class Northern(center: WRefBlock) extends AdjacentBlocks(center,
		north(center)
	)
	class Eastern(center: WRefBlock) extends AdjacentBlocks(center,
		east(center)
	)
	class Western(center: WRefBlock) extends AdjacentBlocks(center,
		west(center)
	)
	class Sandwiching(center: WRefBlock) extends AdjacentBlocks(center,
		up(center),
		down(center)
	)
	class Beside(center: WRefBlock) extends AdjacentBlocks(center,
		north(center),
		east(center),
		south(center),
		west(center)
	)
	class Surrounding(center: WRefBlock) extends AdjacentBlocks(center,
		up(center),
		down(center),
		north(center),
		east(center),
		south(center),
		west(center),
	)
	class Capping(center: WRefBlock) extends AdjacentBlocks(center,
		up(center),
		north(center),
		east(center),
		south(center),
		west(center),
	)
	class Cupping(center: WRefBlock) extends AdjacentBlocks(center,
		down(center),
		north(center),
		east(center),
		south(center),
		west(center),
	)
}

