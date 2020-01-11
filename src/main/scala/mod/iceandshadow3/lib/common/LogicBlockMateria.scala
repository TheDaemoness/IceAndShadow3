package mod.iceandshadow3.lib.common

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.common.model._
import mod.iceandshadow3.lib.compat.block.{CommonBlockVars, Materia}
import mod.iceandshadow3.lib.compat.block.impl.LogicBlockAdapters
import mod.iceandshadow3.lib.compat.file.JsonGenAssetsBlock
import mod.iceandshadow3.lib.compat.id.WIdBlock
import mod.iceandshadow3.lib.compat.loot.{Loot, LootBuilder, WLootContextBlock}
import mod.iceandshadow3.lib.compat.recipe.{ECraftingType, ERecipeSize, IngredientFactory}
import mod.iceandshadow3.lib.misc.{Column2Values, Column3Values, CubeValues}
import mod.iceandshadow3.lib.util.GeneralUtils
import mod.iceandshadow3.lib.{Domain, LogicBlock}

//TODO: Fences.

final class LogicBlockMateria private[common](
	domain: Domain,
	materia: Materia,
	variant: String,
	form: String,
	val relative: WIdBlock,
	loot: LogicBlockMateria => Option[LootBuilder[WLootContextBlock] => Unit],
	modelgenGen: LogicBlockMateria => Option[JsonGenAssetsBlock]
) extends LogicBlock(domain, GeneralUtils.join(GeneralUtils.join(materia.name, variant), form), materia) {
	override def getGenAssetsBlock = modelgenGen(this)
	override def addDrops(what: LootBuilder[WLootContextBlock]): Unit =
		loot(this).fold(super.addDrops(what))(_.apply(what))
}

object LogicBlockMateria {
	def apply(domain: Domain, materia: Materia, variant: String = "") = new Object {
		type LootGenBuilder = LogicBlockMateria => Option[LootBuilder[WLootContextBlock] => Unit]
		val coreName = GeneralUtils.join(s"${domain.name}_${materia.name}", variant)
		val coreRelative = new WIdBlock(IaS3.MODID, coreName)

		private var _textures = CubeValues.builder(coreName).result
		private var _lootgen: LootGenBuilder = _ => None
		def useTextures(what: CubeValues[String]): this.type = {_textures = what;this}
		def useLootGen(what: LootGenBuilder) = {_lootgen = what; this}

		private def form(
			suffix: String, lootgen: LootGenBuilder, modelgenGen: LogicBlockMateria => Option[JsonGenAssetsBlock]
		) = new LogicBlockMateria(domain, materia, variant, suffix, coreRelative, lootgen, modelgenGen)

		def blockCustom(suffix: String = "", modelgenGen: LogicBlockMateria => Option[JsonGenAssetsBlock]) = {
			form(suffix, _lootgen, modelgenGen)
		}
		def block(suffix: String = "") = {
			blockCustom(suffix, logic => Some(JsonGenAssetsBlock.cube(logic, _textures)))
		}

		def stairsCustom(suffix: String = "slab", modelgenGen: LogicBlockMateria => Option[JsonGenAssetsBlock]) = {
			LogicBlockAdapters.stairs(form(suffix, _lootgen, modelgenGen))
		}
		def stairs(suffix: String = "stairs", reuse: ETextureReusePolicy = ETextureReusePolicy.ALL) = {
			stairsCustom(suffix, logic => {
				val joined = GeneralUtils.join(coreName, suffix)
				Some(new JsonGenAssetsBlockStairs(logic, Column3Values(
					if(reuse.ends) _textures.top else s"$joined.top",
					if(reuse.sides) _textures.back else s"$joined.side",
					if(reuse.ends) _textures.bottom else s"$joined.bottom",
				)))
			})
		}

		def slabCustom(suffix: String = "slab", modelgenGen: LogicBlockMateria => Option[JsonGenAssetsBlock]) = {
			LogicBlockAdapters.slab(form(suffix, logic => Some(lb => {
				// TL;DR: Get the lootgen function, or a sensible default otherwise. 
				val lootgen = _lootgen(logic).getOrElse(
					(lootbuild: LootBuilder[WLootContextBlock]) => lootbuild.addOne(Loot(logic))
				)
				// If the mined block is a double, call it twice.
				if(lb.context.state(CommonBlockVars.slab).get.ab) lootgen(lb)
				lootgen(lb)
			}), modelgenGen))
		}
		def slab(suffix: String = "slab", reuse: ETextureReusePolicy = ETextureReusePolicy.ALL) = {
			slabCustom(suffix, logic => {
				val joined = GeneralUtils.join(coreName, suffix)
				Some(new JsonGenAssetsBlockSlab(logic, Column2Values(
					if(reuse.ends) _textures.top else s"$joined.end",
					if(reuse.sides) _textures.back else s"$joined.side"
				)))
			})
		}

		def wallCustom(suffix: String = "wall", modelgenGen: LogicBlockMateria => Option[JsonGenAssetsBlock]) = {
			LogicBlockAdapters.wall(form(suffix, _lootgen, modelgenGen))
		}
		def wall(suffix: String = "wall", reuse: Boolean = true) = {
			wallCustom(suffix, logic => {
				Some(new JsonGenAssetsBlockWall(logic,
					if(reuse) _textures.top else GeneralUtils.join(coreName, suffix)
				))
			})
		}
		def stoneVariants(blockNeedsSuffix: Boolean = false, addRecipes: Boolean = true) = {
			val parent = this
			new Object {
				val block = parent.block(if(blockNeedsSuffix) "block" else "")
				val slab = parent.slab()
				val stairs = parent.stairs()
				val wall = parent.wall()
				if(addRecipes) {
					import IngredientFactory.empty
					ECraftingType.CRAFT_SHAPED(stairs,
						ERecipeSize.THREE_X_THREE, empty, empty, block, empty, block, block, block, block, block
					).unlockDeduce.alterResult(_.setCount(6)).register()
					ECraftingType.STONECUT(stairs, block).unlockDeduce.register()
					ECraftingType.CRAFT_SHAPED(slab,
						block, ERecipeSize.THREE_X_ONE
					).unlockDeduce.alterResult(_.setCount(6)).register()
					ECraftingType.STONECUT(slab, block).alterResult(_.setCount(2)).unlockDeduce.register()
					ECraftingType.CRAFT_SHAPED(wall,
						block, ERecipeSize.THREE_X_TWO
					).unlockDeduce.alterResult(_.setCount(12)).register()
					ECraftingType.STONECUT(wall, block).alterResult(_.setCount(2)).unlockDeduce.register()
				}
			}
		}
	}
}
