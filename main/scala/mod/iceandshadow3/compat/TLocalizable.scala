package mod.iceandshadow3.compat

import net.minecraft.util.text.ITextComponent

trait TLocalizable {
	protected[compat] def getNameTextComponent: ITextComponent
}