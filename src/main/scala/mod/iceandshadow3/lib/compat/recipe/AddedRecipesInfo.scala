package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.WId

class AddedRecipesInfo(factories: scala.collection.mutable.HashMap[WId, RecipeFactory]) {
	def size = factories.size
	def namesRecipes: IterableOnce[String] =
		factories.keys.map(_.name)
	def namesAdvancements: IterableOnce[String] = 
		for(factory <- factories.values; adv <- factory.advancement) yield adv._1
}