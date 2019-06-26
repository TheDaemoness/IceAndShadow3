package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.block.IMateria
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

abstract class BMateria extends IMateria {
	protected[impl] def mcmat: Material
	protected[impl] def sound: SoundType

	override def getBaseOpacity = if (mcmat.isOpaque) 0xff else 0
}