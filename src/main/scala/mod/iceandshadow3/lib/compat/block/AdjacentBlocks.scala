package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.util.collect.IteratorConcat
import net.minecraft.util.Direction

import scala.jdk.CollectionConverters._

sealed abstract class AdjacentBlocks(val central: WBlockView, protected val whats: (WBlockView, Direction)*)
	extends Iterable[WBlockView]
{
	override def iterator = new IteratorConcat[(WBlockView, Direction), WBlockView]({_._1}, whats.iterator.asJava)
}
object AdjacentBlocks {
	protected def up(center: WBlockView) =
		(center.atOffset(0, 1, 0), Direction.DOWN)
	protected def down(center: WBlockView) =
		(center.atOffset(0, -1, 0), Direction.UP)
	protected def south(center: WBlockView) =
		(center.atOffset(0, 0, 1), Direction.NORTH)
	protected def north(center: WBlockView) =
		(center.atOffset(0, 0, -1), Direction.SOUTH)
	protected def east(center: WBlockView) =
		(center.atOffset(1, 0, 0), Direction.WEST)
	protected def west(center: WBlockView) =
		(center.atOffset(-1, 0, 0), Direction.EAST)

	case class Above(center: WBlockView) extends AdjacentBlocks(center,
		up(center)
	)
	case class Below(center: WBlockView) extends AdjacentBlocks(center,
		down(center)
	)
	case class Southern(center: WBlockView) extends AdjacentBlocks(center,
		south(center)
	)
	case class Northern(center: WBlockView) extends AdjacentBlocks(center,
		north(center)
	)
	case class Eastern(center: WBlockView) extends AdjacentBlocks(center,
		east(center)
	)
	case class Western(center: WBlockView) extends AdjacentBlocks(center,
		west(center)
	)
	case class Sandwiching(center: WBlockView) extends AdjacentBlocks(center,
		up(center),
		down(center)
	)
	case class Beside(center: WBlockView) extends AdjacentBlocks(center,
		north(center),
		east(center),
		south(center),
		west(center)
	)
	case class Surrounding(center: WBlockView) extends AdjacentBlocks(center,
		up(center),
		down(center),
		north(center),
		east(center),
		south(center),
		west(center),
	)
	case class Capping(center: WBlockView) extends AdjacentBlocks(center,
		up(center),
		north(center),
		east(center),
		south(center),
		west(center),
	)
	case class Cupping(center: WBlockView) extends AdjacentBlocks(center,
		down(center),
		north(center),
		east(center),
		south(center),
		west(center),
	)
}

