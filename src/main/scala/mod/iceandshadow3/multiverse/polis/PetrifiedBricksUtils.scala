package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.common.LogicBlockMateria
import mod.iceandshadow3.lib.compat.file.BJsonGenAssetsBlock
import mod.iceandshadow3.lib.compat.loot.{BLoot, LootBuilder, WLootContextBlock}
import mod.iceandshadow3.lib.compat.recipe.ERecipeSize.{ONE_X_TWO, TWO_X_ONE, TWO_X_TWO}
import mod.iceandshadow3.lib.compat.recipe.{ECraftingType, IngredientFactory}
import mod.iceandshadow3.lib.misc.CubeValues
import mod.iceandshadow3.multiverse.DomainPolis

object PetrifiedBricksUtils {
	def build = {
		val builder = LogicBlockMateria(DomainPolis, Materias.petrified_brick)
		builder.useTextures(CubeValues.builder(builder.coreName+".y").result)
		new Object {
			val block = builder.useLootGen(lootGen(4)).blockCustom(
				"", logic => Some(BJsonGenAssetsBlock.customSingleModel(logic))
			)
			val slab = builder.useLootGen(lootGen(2)).slab()
			val stairs = builder.useLootGen(lootGen(3)).stairs()
			val wall = builder.useLootGen(lootGen(2)).wall()
		}
	}
	private def lootGen(bricks: Int) = {
		_: LogicBlockMateria => Some((lb: LootBuilder[WLootContextBlock]) => {
			lb.addOne(BLoot.of(DomainPolis.Items.petrified_brick.toWItemType, bricks))
			()
		})
	}
	def recipes(): Unit = {
		val brick = DomainPolis.Items.petrified_brick
		val bricks = DomainPolis.Blocks.petrified_bricks
		ECraftingType.registerAB(brick, bricks.block, TWO_X_TWO)
		ECraftingType.registerAB(brick, bricks.slab, TWO_X_ONE)
		ECraftingType.CRAFT_SHAPED(bricks.stairs, TWO_X_TWO, IngredientFactory.empty, brick, brick, brick).suffix(
			s"ab_from.${brick.namespace}.${brick.name}"
		).unlockDeduce.register()
		ECraftingType.CRAFT_SHAPELESS(brick, bricks.stairs).suffix(
			s"ab_from.${bricks.stairs.namespace}.${bricks.stairs.name}"
		).alterResult(_.setCount(3)).unlockDeduce.register()
		ECraftingType.registerAB(brick, bricks.wall, ONE_X_TWO)
	}
}
