package mod.iceandshadow3.compat.block

import mod.iceandshadow3.basics.block.ECommonBlockType._
import mod.iceandshadow3.basics.block.HarvestMethod
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

abstract class BMateriaLeaves extends BMateria(Material.LEAVES, SoundType.PLANT) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.SHEAR || m == HarvestMethod.BLADE
	override def getBaseHarvestResist: Int = 0
	override def getBaseBlastResist: Float = 1f
	override def getShapes = Set(CUBE)
}

abstract class BMateriaMetal extends BMateria(Material.IRON, SoundType.METAL) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
	override def getShapes = Set(CUBE, SLAB, STAIR, FENCE, PANE)
}

abstract class BMateriaStone extends BMateria(Material.ROCK, SoundType.STONE) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
	override def getShapes = Set(CUBE, SLAB, STAIR, WALL)
}

abstract class BMateriaWood extends BMateria(Material.WOOD, SoundType.WOOD) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.AXE
	override def getShapes = Set(CUBE, SLAB, STAIR, FENCE)
}

abstract class BMateriaGlass extends BMateria(Material.GLASS, SoundType.GLASS) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
	override def getShapes = Set(CUBE, PANE)
	override def isTransparent = true
}

abstract class BMateriaIce extends BMateria(Material.ICE, SoundType.GLASS) {
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
	override def getShapes = Set(CUBE, SLAB, STAIR)
	override def isTransparent = true
}

