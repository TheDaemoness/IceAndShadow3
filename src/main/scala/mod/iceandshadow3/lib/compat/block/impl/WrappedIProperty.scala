package mod.iceandshadow3.lib.compat.block.impl

import net.minecraft.block.BlockState
import net.minecraft.state.IProperty

case class WrappedIProperty[Us, Them <: Comparable[Them]](
	ip: IProperty[Them],
	private val toUs: Them => Us,
	private val toThem: Us => Them
) {
	protected[block] def isIn(bs: BlockState): Boolean = bs.has(ip)
	@throws[IllegalArgumentException]
	protected[block] def in(bs: BlockState) = toUs(bs.get(ip))
	protected[block] def addTo(bs: BlockState, value: Us): BlockState = bs.`with`(ip, toThem(value))
}
