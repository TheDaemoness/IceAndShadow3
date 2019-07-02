package mod.iceandshadow3.lib.compat.util

import net.minecraft.util.text.ITextComponent

trait TLocalized {
	protected[compat] def getLocalizedName: ITextComponent
}
