package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.id.WIdTagItem
import mod.iceandshadow3.lib.compat.item.{WItem, WItemType}
import net.minecraftforge.registries.ForgeRegistries

import scala.collection.mutable
import scala.jdk.CollectionConverters._

abstract class CraftGraphAnalysis[T <: Object, Arg](
	craftmap: WItemType => java.util.Set[CraftingSummary],
	protected val arg: Arg
)
extends (WItem => T) {
	protected def defaultValue(input: WItem): T
	/** Called with possibility of an Ingredient*/
	protected def resolveWildcard(options: Iterable[T]): T
	/** Called for the folded inputs */
	protected def combine(values: Iterable[T], output: Int): T
	/** Called with the default input and the combined values of all the recipes*/
	protected def resolveFinal(default: T, fromRecipes: Iterable[T]): T
	protected def shouldFollow(what: CraftingSummary): Boolean = true
	val values = new mutable.HashMap[WItem, T]
	val tagNoInfer = WIdTagItem("iceandshadow3:no_infer")
	private val map = {
		def dfs(what: WItemType): Unit = {
			val default = defaultValue(what)
			values.put(what, default)
			if(!tagNoInfer.unapply(what)) {
				val list = new mutable.ListBuffer[T]
				for (recipe <- craftmap(what).asScala) {
					if (shouldFollow(recipe)) {
						for (input <- recipe.inputItems) if (!values.contains(input)) dfs(input)
						list += recipe.evaluate[T](
							tolookup => values.getOrElse(tolookup, defaultValue(tolookup)),
							resolveWildcard,
							combine
						)
					}
				}
				values.update(what, resolveFinal(default, list))
			}
		}
		ForgeRegistries.ITEMS.getValues.asScala.map(WItemType.apply).foreach(dfs)
		values.toMap
	}

	override def apply(what: WItem) = map.getOrElse(what, {
		IaS3.logger().warn(s"Unregistered item queried in $this: $what")
		defaultValue(what)
	})
}
