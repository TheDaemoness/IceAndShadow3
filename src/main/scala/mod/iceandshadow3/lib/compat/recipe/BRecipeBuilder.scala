package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.Registrar
import mod.iceandshadow3.lib.compat.file.BRecipeUnlockGen
import mod.iceandshadow3.lib.compat.item.WItemStack

abstract class BRecipeBuilder(
	protected val ect: ECraftingType,
	craftResult: => CraftResult
) {
	protected var unlock: BRecipeUnlockGen = BRecipeUnlockGen.standard()
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
		unlock = BRecipeUnlockGen.standard()
		this
	}
	final def unlockDeduce: this.type = {
		unlock = BRecipeUnlockGen.standard(true)
		this
	}
	final def unlock(gen: BRecipeUnlockGen): this.type = {
		unlock = gen
		this
	}
	protected def factory(nrm: NewRecipeMetadata): RecipeFactory
	final def register(): Boolean = Registrar.addRecipeFactory(factory(
		new NewRecipeMetadata(craftResult, ect, name, nameIsSuffix, group, resultMod)
	))
}
