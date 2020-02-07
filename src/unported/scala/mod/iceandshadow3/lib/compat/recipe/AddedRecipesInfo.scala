package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.id.WId

class AddedRecipesInfo(factories: scala.collection.mutable.HashMap[WId, RecipeFactory]) {
	def size = factories.size
	def namesRecipes = factories.keys.map(_.name)
	def namesUnlocks = factories.map(_._2.advancementName).filter(!_.isEmpty)

	// Methods to simplify use from the tests written in Java.
	import scala.jdk.CollectionConverters._
	def namesRecipesIterator = namesRecipes.iterator.asJava
	def namesUnlocksIterator = namesUnlocks.iterator.asJava
}