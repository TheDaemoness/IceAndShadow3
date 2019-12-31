package mod.iceandshadow3.lib.common

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.common.model._
import mod.iceandshadow3.lib.compat.WIdBlock
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
	variant: String,
	form: String,
	val relative: WIdBlock,
	modelgenGen: LogicBlockMateria => Option[BJsonGenAssetsBlock]
) extends LogicBlock(domain, GeneralUtils.join(GeneralUtils.join(materia.name, variant), form), materia) {
	override def getGenAssetsBlock = modelgenGen(this)
}

object LogicBlockMateria {
	def apply(domain: BDomain, materia: Materia, variant: String = "") = new Object {
		private val coreName =
			GeneralUtils.join(s"${domain.name}_${materia.name}", variant)
		val coreRelative = new WIdBlock(IaS3.MODID, coreName)
		private var _textures = CubeValues.builder(coreName).result
		def useTextures(what: CubeValues[String]): this.type = {
			_textures = what
			this
		}
		private def form(suffix: String, modelgenGen: LogicBlockMateria => Option[BJsonGenAssetsBlock]) =
			new LogicBlockMateria(domain, materia, variant, suffix, coreRelative, modelgenGen)

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
				val joined = GeneralUtils.join(coreName, suffix)
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
		def slab(suffix: String = "slab", reuse: ETextureReusePolicy = ETextureReusePolicy.ALL) = {
			slabCustom(suffix, logic => {
				val joined = GeneralUtils.join(coreName, suffix)
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
					if(reuse) _textures.top else GeneralUtils.join(coreName, suffix)
				))
			})
		}
	}
	def stoneVariants(domain: BDomain, materia: Materia, variant: String = "", blockNeedsSuffix: Boolean = false) = {
		val factory = apply(domain, materia, variant)
		new Object {
			val block = factory.block(if(blockNeedsSuffix) "block" else "")
			val slab = factory.slab()
			val stairs = factory.stairs()
			val wall = factory.wall()
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
