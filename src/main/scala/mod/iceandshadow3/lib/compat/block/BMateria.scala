package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.block.IMateria
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

abstract class BMateria(protected[compat] val mcmat: Material, protected[compat] val sound: SoundType)
extends IMateria {
	override def getBaseOpacity = if (mcmat.isOpaque) 0xff else 0
}