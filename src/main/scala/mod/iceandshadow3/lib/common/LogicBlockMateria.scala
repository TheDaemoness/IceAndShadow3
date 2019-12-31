package mod.iceandshadow3.lib.common

import mod.iceandshadow3.lib.common.model._
import mod.iceandshadow3.lib.compat.block.Materia
import mod.iceandshadow3.lib.compat.block.impl.LogicBlockAdapters
import mod.iceandshadow3.lib.compat.file.BJsonGenAssetsBlock
import mod.iceandshadow3.lib.compat.recipe.{ECraftingType, ERecipeSize, IngredientFactory}
import mod.iceandshadow3.lib.misc.{Column2Values, Column3Values, CubeValues}
import mod.iceandshadow3.lib.util.GeneralUtils
import mod.iceandshadow3.lib.{BDomain, LogicBlock}

//TODO: Fences.

final class LogicBlockMateria private[common](
	domain: BDomain,
	materia: Materia,
	suffix: String,
	modelgenGen: LogicBlockMateria => Option[BJsonGenAssetsBlock]
) extends LogicBlock(domain, GeneralUtils.join(materia.name, suffix), materia) {
	override def getGenAssetsBlock = modelgenGen(this)
	def coreName = LogicBlockMateria.coreName(domain, materia)
}

object LogicBlockMateria {
	def coreName(domain: BDomain, materia: Materia) =  GeneralUtils.join(domain.name, materia.name)
	def apply(domain: BDomain, materia: Materia) = new Object {
		private var _textures = CubeValues.builder(coreName(domain, materia)).result
		def useTextures(what: CubeValues[String]): this.type = {
			_textures = what
			this
		}
		private def form(suffix: String, modelgenGen: LogicBlockMateria => Option[BJsonGenAssetsBlock]) =
			new LogicBlockMateria(domain, materia, suffix, modelgenGen)

		def blockCustom(suffix: String = "", modelgenGen: LogicBlockMateria => Option[BJsonGenAssetsBlock]) = {
			form(suffix, modelgenGen)
		}
		def block(suffix: String = "") = {
			blockCustom(suffix, logic => Some(BJsonGenAssetsBlock.cube(logic, _textures)))
		}
		def stairsCustom(suffix: String = "slab", modelgenGen: LogicBlockMateria => Option[BJsonGenAssetsBlock]) = {
			LogicBlockAdapters.stairs(form(suffix, modelgenGen))
		}
		def stairs(suffix: String = "stairs", reuse: ETextureReusePolicy = ETextureReusePolicy.ALL) = {
			stairsCustom(suffix, logic => {
				val joined = GeneralUtils.join(coreName(domain, materia), suffix)
				Some(new JsonGenAssetsBlockStairs(logic, Column3Values(
					if(reuse.ends) _textures.top else s"$joined.top",
					if(reuse.sides) _textures.back else s"$joined.side",
					if(reuse.ends) _textures.bottom else s"$joined.bottom",
				)))
			})
		}

		def slabCustom(suffix: String = "slab", modelgenGen: LogicBlockMateria => Option[BJsonGenAssetsBlock]) = {
			LogicBlockAdapters.slab(form(suffix, modelgenGen))
		}
		def slab(suffix: String = "slab", reuse: ETextureReusePolicy = ETextureReusePolicy.ENDS) = {
			slabCustom(suffix, logic => {
				val joined = GeneralUtils.join(coreName(domain, materia), suffix)
				Some(new JsonGenAssetsBlockSlab(logic, Column2Values(
					if(reuse.ends) _textures.top else s"$joined.end",
					if(reuse.sides) _textures.back else s"$joined.side"
				)))
			})
		}

		def wallCustom(suffix: String = "wall", modelgenGen: LogicBlockMateria => Option[BJsonGenAssetsBlock]) = {
			LogicBlockAdapters.wall(form(suffix, modelgenGen))
		}
		def wall(suffix: String = "wall", reuse: Boolean = true) = {
			wallCustom(suffix, logic => {
				Some(new JsonGenAssetsBlockWall(logic,
					if(reuse) _textures.top else GeneralUtils.join(coreName(domain, materia), suffix)
				))
			})
		}
	}
	def stoneVariants(domain: BDomain, materia: Materia) = {
		val factory = apply(domain, materia)
		new Object {
			val blocks = factory.block()
			val stairs = factory.stairs()
			val slab = factory.slab()
			val wall = factory.wall()
			import IngredientFactory.empty
			ECraftingType.CRAFT_SHAPED(stairs,
				ERecipeSize.THREE_X_THREE, empty, empty, blocks, empty, blocks, blocks, blocks, blocks, blocks
			).unlockDeduce.alterResult(_.setCount(6)).register()
			ECraftingType.STONECUT(stairs, blocks).unlockDeduce.register()
			ECraftingType.CRAFT_SHAPED(slab,
				blocks, ERecipeSize.THREE_X_ONE
			).unlockDeduce.alterResult(_.setCount(6)).register()
			ECraftingType.STONECUT(slab, blocks).alterResult(_.setCount(2)).unlockDeduce.register()
			ECraftingType.CRAFT_SHAPED(wall,
				blocks, ERecipeSize.THREE_X_TWO
			).unlockDeduce.alterResult(_.setCount(12)).register()
			ECraftingType.STONECUT(wall, blocks).unlockDeduce.register()
		}
	}
}
