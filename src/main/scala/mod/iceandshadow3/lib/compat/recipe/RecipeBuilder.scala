package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.Registrar
import mod.iceandshadow3.lib.compat.file.RecipeUnlockGen
import mod.iceandshadow3.lib.compat.item.WItemStack

abstract class RecipeBuilder(
	protected val ect: ECraftingType,
	craftResult: => BCraftResult
) {
	protected var unlock: RecipeUnlockGen = RecipeUnlockGen.standard()
	protected var nameIsSuffix: Boolean = true
	protected var name: String = ""
	protected var group: Option[String] = None
	protected var resultMod: WItemStack => Unit = _ => ()
	final def name(string: String): this.type = {
		nameIsSuffix = false
		name = string
		this
	}
	final def suffix(string: String): this.type = {
		nameIsSuffix = true
		name = string
		this
	}
	final def nameDefault: this.type = {
		nameIsSuffix = true
		name = ""
		this
	}
	final def group(string: String): this.type = {
		group = Some(string)
		this
	}
	final def groupDefault: this.type = {
		group = None
		this
	}
	final def alterResult(fn: WItemStack => Unit): this.type = {
		resultMod = fn
		this
	}
	final def unlockDefault: this.type = {
		unlock = RecipeUnlockGen.standard()
		this
	}
	final def unlockDeduce: this.type = {
		unlock = RecipeUnlockGen.standard(true)
		this
	}
	final def unlock(gen: RecipeUnlockGen): this.type = {
		unlock = gen
		this
	}
	protected def factory(nrm: NewRecipeMetadata): RecipeFactory
	final def register(): Boolean = Registrar.addRecipeFactory(factory(
		new NewRecipeMetadata(craftResult, ect, name, nameIsSuffix, group, resultMod)
	))
}
