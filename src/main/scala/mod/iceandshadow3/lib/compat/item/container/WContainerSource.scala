package mod.iceandshadow3.lib.compat.item.container

import java.util.OptionalInt

import javax.annotation.Nullable
import mod.iceandshadow3.lib.compat.block.WBlockRef
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer
import net.minecraft.inventory.container.{INamedContainerProvider, SimpleNamedContainerProvider, WorkbenchContainer}
import net.minecraft.util.text.TranslationTextComponent

class WContainerSource(@Nullable private[compat] val expose: INamedContainerProvider) {
	def isEmpty = expose == null
	def isDefined = expose != null
	def openAs(whom: WEntityPlayer): OptionalInt = whom.player.openContainer(expose)
}
object WContainerSource {
	val none = new WContainerSource(null)

	def crafting(name: String): WContainerSource = new WContainerSource(
		new SimpleNamedContainerProvider((x, pl, _) => {
			new WorkbenchContainer(x, pl)
		}, new TranslationTextComponent(name))
	)
	//TODO: Usability range check that ISN'T hardcoded.
	def crafting(where: WBlockRef): WContainerSource = crafting(where.id.translationKey)
}
