package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.WId
import mod.iceandshadow3.lib.compat.item.WItemStack

private[compat] final class NewRecipeMetadata(
	what: CraftResult,
	method: ECraftingType,
	customName: String, customNameIsSuffix: Boolean,
	groupOverride: Option[String],
	mod: WItemStack => Unit
) {
	def this(method: ECraftingType, customName: String) = this(
		null, method,
		customName, true, Some(""), _ => ()
	)
	def result = if(what != null) {val first = what.toItemStack; mod(first); first} else WItemStack.empty
	def group = groupOverride.fold(what.id.toString)(_.toLowerCase)
	val name = if(customNameIsSuffix) NewRecipeMetadata.standardName(method, what.id, customName) else customName
}
private[compat] object NewRecipeMetadata {
	/** Constructs a NewRecipeMetadata without most of its arguments. */
	def dynamic(method: ECraftingType, customName: String) = new NewRecipeMetadata(
		null, method, customName, false, Some(""), _ => ()
	)
	private def standardName(
		method: ECraftingType,
		what: WId,
		suffix: String
	): String = {
		val resultName = s"${what.namespace}.${what.name}"
		if (suffix.isEmpty) s"${method.name}.$resultName"
		else s"${method.name}.$resultName.$suffix"
	}
}
