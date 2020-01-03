package mod.iceandshadow3.lib.compat.block.impl

import net.minecraft.block.BlockState
import net.minecraft.state.IProperty

case class WIProperty[Us, Them <: Comparable[Them]](
	private[block] val expose: IProperty[Them],
	private val toUs: Them => Us,
	private val toThem: Us => Them
) {
	protected[block] def isIn(bs: BlockState): Boolean = bs.has(expose)
	@throws[IllegalArgumentException]
	protected[block] def in(bs: BlockState) = toUs(bs.get(expose))
	protected[block] def addTo(bs: BlockState, value: Us): BlockState = bs.`with`(expose, toThem(value))
}
