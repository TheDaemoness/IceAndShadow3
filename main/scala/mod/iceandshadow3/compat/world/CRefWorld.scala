package mod.iceandshadow3.compat.world

import net.minecraft.world.World

class CRefWorld(private[compat] val world: World) extends TCRefWorld {
	override def getWorld(): World = world
	//WARNING: This stubbiness is deliberate.
	//Most functionality should NOT go here, but in TCRefWorld.
}
