package mod.iceandshadow3.compat.block

import mod.iceandshadow3.util.IteratorConcat
import net.minecraft.util.EnumFacing

import scala.collection.JavaConverters._

sealed abstract class AdjacentBlocks(val central: WBlockView, protected val whats: (WBlockView, EnumFacing)*)
	extends Iterable[WBlockView]
{
	def areSidesSolid = whats.forall(pair => pair._1.isSideSolid(pair._2))
	override def iterator = new IteratorConcat[(WBlockView, EnumFacing), WBlockView]({_._1}, whats.iterator.asJava)
}
object AdjacentBlocks {
	protected def up(center: WBlockView) =
		(center.atOffset(0, 1, 0), EnumFacing.DOWN)
	protected def down(center: WBlockView) =
		(center.atOffset(0, -1, 0), EnumFacing.UP)
	protected def south(center: WBlockView) =
		(center.atOffset(0, 0, 1), EnumFacing.NORTH)
	protected def north(center: WBlockView) =
		(center.atOffset(0, 0, -1), EnumFacing.SOUTH)
	protected def east(center: WBlockView) =
		(center.atOffset(1, 0, 0), EnumFacing.WEST)
	protected def west(center: WBlockView) =
		(center.atOffset(-1, 0, 0), EnumFacing.EAST)

	class Above(center: WBlockView) extends AdjacentBlocks(center,
		up(center)
	)
	class Below(center: WBlockView) extends AdjacentBlocks(center,
		down(center)
	)
	class Southern(center: WBlockView) extends AdjacentBlocks(center,
		south(center)
	)
	class Northern(center: WBlockView) extends AdjacentBlocks(center,
		north(center)
	)
	class Eastern(center: WBlockView) extends AdjacentBlocks(center,
		east(center)
	)
	class Western(center: WBlockView) extends AdjacentBlocks(center,
		west(center)
	)
	class Sandwiching(center: WBlockView) extends AdjacentBlocks(center,
		up(center),
		down(center)
	)
	class Beside(center: WBlockView) extends AdjacentBlocks(center,
		north(center),
		east(center),
		south(center),
		west(center)
	)
	class Surrounding(center: WBlockView) extends AdjacentBlocks(center,
		up(center),
		down(center),
		north(center),
		east(center),
		south(center),
		west(center),
	)
	class Capping(center: WBlockView) extends AdjacentBlocks(center,
		up(center),
		north(center),
		east(center),
		south(center),
		west(center),
	)
	class Cupping(center: WBlockView) extends AdjacentBlocks(center,
		down(center),
		north(center),
		east(center),
		south(center),
		west(center),
	)
}

