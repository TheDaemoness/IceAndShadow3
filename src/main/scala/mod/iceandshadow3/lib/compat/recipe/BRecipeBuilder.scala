package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.Registrar
import mod.iceandshadow3.lib.compat.item.WItemStack

abstract class BRecipeBuilder(
	protected val ect: ECraftingType,
	protected val craftResult: BCraftResult
) {
	protected var unlock: Either[Boolean, Option[String]] = Left(true)
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
	final def defaultName: this.type = {
		nameIsSuffix = true
		name = ""
		this
	}
	final def group(string: String): this.type = {
		group = Some(string)
		this
	}
	final def defaultGroup: this.type = {
		group = None
		this
	}
	final def transformResult(fn: WItemStack => Unit): this.type = {
		resultMod = fn
		this
	}
	final def defaultUnlock: this.type = {
		unlock = Left(true)
		this
	}
	final def customUnlock: this.type = {
		unlock = Right(None)
		this
	}
	final def customUnlock(id: String): this.type = {
		unlock = Right(Some(id))
		this
	}
	final def noUnlock: this.type = {
		unlock = Left(false)
		this
	}
	protected def factory(nrm: RecipeMetadata): RecipeFactory
	final def register(): Boolean = {
		val factoryObj = factory(new RecipeMetadata(craftResult, ect, name, nameIsSuffix, group, resultMod))
		Registrar.addRecipeFactory(unlock match {
			case Left(hasUnlock) => if(!hasUnlock) factoryObj.withNoUnlock() else factoryObj
			case Right(name) => name.fold(factoryObj.withCustomUnlock())(factoryObj.withCustomUnlock)
		})
	}
}
