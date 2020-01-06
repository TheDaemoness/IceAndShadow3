package mod.iceandshadow3.lib.block

import mod.iceandshadow3.lib.compat.block.{WBlockRef, WBlockState}
import mod.iceandshadow3.lib.util.E3vl

abstract class BHandlerComparator {
	/** Checked first if we can skip running hasValue(WBlockState). */
	val hasValue: E3vl = E3vl.TRUE
	def hasValue(what: WBlockState) = true
	def value(what: WBlockRef): Int
}
object BHandlerComparator {
	val none = new BHandlerComparator {
		override val hasValue = E3vl.FALSE
		override def hasValue(what: WBlockState) = false
		override def value(what: WBlockRef) = 0
	}
	def apply(fn: WBlockRef => Int) = new BHandlerComparator {
		override def value(what: WBlockRef) = fn(what)
	}
}
