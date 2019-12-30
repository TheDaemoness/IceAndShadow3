package mod.iceandshadow3.lib.common

import mod.iceandshadow3.lib.compat.block.Materia
import mod.iceandshadow3.lib.compat.block.impl.LogicBlockAdapters
import mod.iceandshadow3.lib.compat.recipe.{ECraftingType, ERecipeSize, IngredientFactory}
import mod.iceandshadow3.lib.{BDomain, LogicBlock}

//TODO: Fences.

final class LogicBlockMateria private[common](
	domain: BDomain,
	materia: Materia,
	suffix: String
) extends LogicBlock(domain, if(suffix.isEmpty) materia.name else s"${materia.name}_$suffix", materia)
object LogicBlockMateria {
	def apply(domain: BDomain, materia: Materia) = new Object {
		def block(suffix: String = "") = new LogicBlockMateria(domain, materia, suffix)
		def stairs(suffix: String = "stairs") = LogicBlockAdapters.stairs(block(suffix))
		def slab(suffix: String = "slab") = LogicBlockAdapters.slab(block(suffix))
		def wall(suffix: String = "wall") = LogicBlockAdapters.wall(block(suffix))
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
			).unlockDeduce.alterResult(_.setCount(6)).register()
			ECraftingType.STONECUT(wall, blocks).unlockDeduce.register()
		}
	}
}
