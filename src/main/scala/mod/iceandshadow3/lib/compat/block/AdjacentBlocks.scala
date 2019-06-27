package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.compat.util.IWrapper
import mod.iceandshadow3.util.collect.IteratorConcat
import net.minecraft.util.Direction

import scala.collection.JavaConverters._

sealed abstract class AdjacentBlocks(val central: WBlockView, protected val whats: (WBlockView, Direction)*)
	extends Iterable[WBlockView]
{
	override def iterator = new IteratorConcat[(WBlockView, Direction), WBlockView]({_._1}, whats.iterator.asJava)

	def one = new IWrapper[WBlockView] {
		override def isAny(queries: (WBlockView => Boolean)*): Boolean = {
			for(view <- iterator) if(view.isAny(queries:_*)) return true
			false
		}
		def isAll(queries: (WBlockView => Boolean)*): Boolean = {
			for(view <- iterator) if(view.isAll(queries:_*)) return true
			false
		}
	}

	def each = new IWrapper[WBlockView] {
		def isAny(queries: (WBlockView => Boolean)*): Boolean = {
			for(view <- iterator) if(!view.isAny(queries:_*)) return false
			true
		}
		override def isAll(queries: (WBlockView => Boolean)*): Boolean = {
			for(view <- iterator) if(!view.isAll(queries:_*)) return false
			true
		}
	}

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

