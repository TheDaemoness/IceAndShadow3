package mod.iceandshadow3.multiverse

import mod.iceandshadow3.lib.common.LogicBlockMateria
import mod.iceandshadow3.lib.compat.item.WRarity
import mod.iceandshadow3.lib.util.Color
import mod.iceandshadow3.lib.{BDomain, LogicItemMulti}
import mod.iceandshadow3.multiverse.gaia.ELivingstoneTypes
import mod.iceandshadow3.multiverse.polis._

object DomainPolis extends BDomain("polis") {
	val Blocks = new {
		val stones = ELivingstoneTypes.values().map(
			variant => LogicBlockMateria(DomainPolis, Materias.stone, variant.name).stoneVariants()
		)
		val petrified_bricks = PetrifiedBricksUtils.build
		val moonstone_dust = new LBMoonstoneDust
		val table_crafting = new LBTableCrafting
		val bin_storage = new LBBinStorage("bin_storage", Materias.petrified_brick)
	}
	val Items = new {
		val petrified_brick = new LogicItemMulti(DomainPolis, "petrified_brick")
		val moonstone_dust = new LIMoonstoneDust
	}

	override def isHostileTo(other: BDomain): Boolean = other == DomainNyx || other == DomainGaia
	override def tierToRarity(tier: Int): WRarity = WRarity.COMMON

	override def color = Color.CYAN
	override protected def baseStrength: Float = 7f
}
