package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.base.{BLogic, TLogicWithItem, TNamed}
import mod.iceandshadow3.lib.compat.{WId, WIdItem}
import mod.iceandshadow3.lib.compat.item.WItemStack

sealed abstract class NewRecipeMetadata {
	def result: WItemStack
	def group: String
	def name: String
	final def withName(what: String): NewRecipeMetadata = new NewRecipeMetadata {
		override def result = NewRecipeMetadata.this.result
		override def group = NewRecipeMetadata.this.group
		override def name = what
	}
	final def withGroup(what: String): NewRecipeMetadata = new NewRecipeMetadata {
		override def result = NewRecipeMetadata.this.result
		override def group = what
		override def name = NewRecipeMetadata.this.name
	}
}
object NewRecipeMetadata {
	import scala.util.chaining._

	private def standardName(
		method: ECraftingType,
		what: WId,
		suffix: Option[String] = None
	): String = suffix.fold(
			s"${method.name}.$what"
		)(suffixString =>
			s"${method.name}.$what.$suffixString"
		)

	def fromLogic(
		what: BLogic with TLogicWithItem,
		method: ECraftingType,
		suffix: Option[String] = None,
		mod: WItemStack => Unit = _ => ()
	): NewRecipeMetadata = new NewRecipeMetadata {
		override lazy val result = what.toWItemStack.tap(mod)
		override def group = what.id.toString
		override val name = standardName(method, what.id, suffix)
	}

	def fromNamed(
		what: TNamed[WIdItem],
		method: ECraftingType,
		suffix: Option[String] = None,
		mod: WItemStack => Unit = _ => ()
	): NewRecipeMetadata = new NewRecipeMetadata {
		override lazy val result = what.id.unapply.get.asWItemStack().tap(mod) //We want an exception to fire here.
		override def group = what.id.toString
		override val name = standardName(method, what.id, suffix)
	}
}
