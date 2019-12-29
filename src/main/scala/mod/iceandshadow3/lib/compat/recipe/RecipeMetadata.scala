package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.WId
import mod.iceandshadow3.lib.compat.item.WItemStack

private[compat] final class RecipeMetadata(
	what: BCraftResult,
	method: ECraftingType,
	customName: String, customNameIsSuffix: Boolean,
	groupOverride: Option[String],
	mod: WItemStack => Unit
) {
	def this(method: ECraftingType, customName: String) = this(
		BCraftResult(WItemStack.empty), method,
		customName, true, Some(""), _ => ()
	)
	lazy val result = if(what != null) Some({val first = what.toItemStack; mod(first); first}) else None
	def group = groupOverride.fold(what.id.toString)(_.toLowerCase)
	val name = if(customNameIsSuffix) RecipeMetadata.standardName(method, what.id, customName) else customName
}
private[compat] object RecipeMetadata {
	def dynamic(method: ECraftingType, customName: String) = new RecipeMetadata(
		null, method, customName, false, Some(""), _ => ()
	)
	private def standardName(
		method: ECraftingType,
		what: WId,
		suffix: String
	): String = {
		if (suffix.isEmpty) s"${method.name}.$what"
		else s"${method.name}.$what.$suffix"
	}
}
