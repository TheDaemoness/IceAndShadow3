package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.block.HarvestMethod
import mod.iceandshadow3.lib.compat.block.Materia

object Materias {
	import Materia.builder
	val livingstone =
		builder(Materia.stone).hardness(3f).harvestLevel(1)("livingstone")
	val navistra_stone =
		builder(livingstone).blastproof.hardness(50f).harvestLevel(5).slipperiness(0.9f)("navistra_stone")
	val navistra_bedrock =
		builder(navistra_stone).indestructible("navistra_bedrock")
	val petrified_wood =
		builder(Materia.stone).hardness(5f).resistFactor(2f).harvestLevel(2)("petrified_wood")
	val petrified_leaves =
		builder(livingstone).sounds(Materia.gravel).
			hardness(0.6f).harvestWith(HarvestMethod.AXE).harvestLevel(1)("petrified_leaves")
}
