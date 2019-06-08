package mod.iceandshadow3.basics.block

import javax.annotation.Nullable
import net.minecraftforge.common.ToolType

object HarvestMethod {
	private val lookups = new java.util.HashMap[String, HarvestMethod]
	val HAND = new HarvestMethod("hand", null)
	val PICKAXE = new HarvestMethod("pickaxe")
	val SHOVEL = new HarvestMethod("shovel")
	//Let's not have a spade repeat.
	val AXE = new HarvestMethod("axe")
	val BLADE = new HarvestMethod("sword")
	val SHEAR = new HarvestMethod("shear", null)
	val EXPLOSION = new HarvestMethod("explosion", null)

	def get(name: String) = {
		var retval = lookups.get(name)
		if (retval == null) retval = new HarvestMethod(name, name)
		retval
	}

	def get(tt: ToolType): HarvestMethod = {
		if (tt == null) return HAND
		if (tt eq ToolType.PICKAXE) return PICKAXE
		if (tt eq ToolType.SHOVEL) return SHOVEL
		if (tt eq ToolType.AXE) return AXE
		get(tt.getName)
	}
}

case class HarvestMethod private(name: String, @Nullable classname: String) {
	if (classname != null) HarvestMethod.lookups.putIfAbsent(classname, this)

	def this(string: String) {
		this(string, string)
	}

	override def toString = name
}