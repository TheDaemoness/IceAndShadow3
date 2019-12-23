package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.compat.block.Materia

object Materias {
	import mod.iceandshadow3.lib.compat.block.Materia._
	import mod.iceandshadow3.multiverse._

	val stone = builder(gaia.Materias.livingstone).hardness(2.5f)("stone_polished")
	def petrified_brick = gaia.Materias.petrified_wood
	val moonstone_dust =
		builder(Materia.plasma).luma(9).opacity(0).transparent(true)("moonstone_dust")
}
