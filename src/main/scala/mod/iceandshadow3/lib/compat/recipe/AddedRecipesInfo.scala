package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.WId

class AddedRecipesInfo(factories: scala.collection.mutable.HashMap[WId, RecipeFactory]) {
	def size = factories.size
	def namesRecipes = factories.keys.map(_.name)
	def namesAdvancements = for(factory <- factories.values; adv <- factory.advancement) yield adv._1

	// Methods to simplify use from the tests written in Java.
	import scala.jdk.CollectionConverters._
	def namesRecipesIterator = namesRecipes.iterator.asJava
	def namesAdvancementsIterator = namesAdvancements.iterator.asJava
}