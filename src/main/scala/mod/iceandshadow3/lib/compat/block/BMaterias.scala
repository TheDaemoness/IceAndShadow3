package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.block.ECommonBlockType._
import mod.iceandshadow3.lib.block.HarvestMethod
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

abstract class BMateriaLeaves extends BMateria {
	protected[block] lazy val mcmat = Material.LEAVES
	protected[block] lazy val sound = SoundType.PLANT
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.SHEAR || m == HarvestMethod.BLADE
	override def getBaseHarvestResist: Int = 0
	override def getBaseBlastResist: Float = 1f
	override def getShapes = Set(CUBE)
}

abstract class BMateriaPlasma extends BMateria {
	protected[block] lazy val mcmat = Material.FIRE
	protected[block] lazy val sound = SoundType.SLIME
	override def isToolClassEffective(m: HarvestMethod): Boolean = false
	override def isNonSolid = true
	override def isTransparent = true
	override def getBaseOpacity = 1
}

abstract class BMateriaMetal extends BMateria {
	protected[block] lazy val mcmat = Material.IRON
	protected[block] lazy val sound = SoundType.METAL
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
	override def getShapes = Set(CUBE, SLAB, STAIR, FENCE, PANE)
}

abstract class BMateriaStone extends BMateria {
	protected[block] lazy val mcmat = Material.ROCK
	protected[block] lazy val sound = SoundType.STONE
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
	override def getShapes = Set(CUBE, SLAB, STAIR, WALL)
}

abstract class BMateriaWood extends BMateria {
	protected[block] lazy val mcmat = Material.WOOD
	protected[block] lazy val sound = SoundType.WOOD
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.AXE
	override def getShapes = Set(CUBE, SLAB, STAIR, FENCE)
}

abstract class BMateriaGlass extends BMateria {
	protected[block] lazy val mcmat = Material.GLASS
	protected[block] lazy val sound = SoundType.GLASS
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
	override def getShapes = Set(CUBE, PANE)
	override def isTransparent = true
}

abstract class BMateriaIce extends BMateria {
	protected[block] lazy val mcmat = Material.ICE
	protected[block] lazy val sound = SoundType.GLASS
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.PICKAXE
	override def getShapes = Set(CUBE, SLAB, STAIR)
	override def isTransparent = true
}

abstract class BMateriaGravel extends BMateria {
	protected[block] lazy val mcmat = Material.SAND
	protected[block] lazy val sound = SoundType.GROUND
	override def isToolClassEffective(m: HarvestMethod): Boolean =
		m == HarvestMethod.SHOVEL
	override def getShapes = Set(CUBE)
	override def getBaseHardness = 0.6f
	override def getBaseHarvestResist = -1
}
