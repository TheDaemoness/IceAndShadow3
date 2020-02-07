package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.block.HarvestMethod
import mod.iceandshadow3.lib.compat.block.Materia

object Materias {
	import mod.iceandshadow3.lib.compat.block.Materia._
	import mod.iceandshadow3.multiverse._

	val stone = builder(gaia.Materias.livingstone).hardness(2.5f)("stone")
	val petrified_brick = builder(gaia.Materias.petrified_wood
		).hardness(1f).resistFactor(gaia.Materias.petrified_wood.resistance).harvestLevel(-1)("petrified_bricks")
	val moonstone_dust =
		builder(Materia.plasma).luma(9).opacity(0).transparent(true)("moonstone_dust")
}
