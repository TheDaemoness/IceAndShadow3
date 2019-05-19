package mod.iceandshadow3.compat.block

import mod.iceandshadow3.basics.util.EBlockShape._
import mod.iceandshadow3.basics.util.HarvestMethod
import net.minecraft.block.material.Material

abstract class BMateriaLeaves extends BMateria(Material.LEAVES) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.SHEAR || m == HarvestMethod.BLADE
	override def getBaseHarvestResist: Int = 0
	override def getBaseBlastResist: Float = 1f
	override def getShapes = Set(CUBE)
}

abstract class BMateriaMetal extends BMateria(Material.IRON) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
	override def getShapes = Set(CUBE, SLAB, STAIR, FENCE, PANE)
}

abstract class BMateriaStone extends BMateria(Material.ROCK) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
	override def getShapes = Set(CUBE, SLAB, STAIR, WALL)
}

abstract class BMateriaWood extends BMateria(Material.WOOD) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.AXE
	override def getShapes = Set(CUBE, SLAB, STAIR, FENCE)
}

abstract class BMateriaGlass extends BMateria(Material.GLASS) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
	override def getShapes = Set(CUBE, PANE)
}

