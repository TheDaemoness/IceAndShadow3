package mod.iceandshadow3.compat

import net.minecraft.util.text.ITextComponent

trait TNamed {
	protected[compat] def getNameTextComponent: ITextComponent
}