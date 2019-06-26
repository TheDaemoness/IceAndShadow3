package mod.iceandshadow3.lib.compat.util

import net.minecraft.util.text.ITextComponent

trait TNamed {
	protected[compat] def getNameTextComponent: ITextComponent
}
