package mod.iceandshadow3.compat.block

import mod.iceandshadow3.basics.HarvestMethod;
import net.minecraft.block.material.Material;

abstract class BMateriaLeaves extends BMateria(Material.LEAVES) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.SHEAR || m == HarvestMethod.BLADE
	override def getBaseHarvestResist(): Int = 0
	override def getBaseBlastResist(): Float = 1f
}

abstract class BMateriaMetal extends BMateria(Material.IRON) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
}

abstract class BMateriaStone extends BMateria(Material.ROCK) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
}

abstract class BMateriaWood extends BMateria(Material.WOOD) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.AXE
}
