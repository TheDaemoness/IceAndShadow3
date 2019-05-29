package mod.iceandshadow3.forge

import mod.iceandshadow3.basics.BDimension
import mod.iceandshadow3.compat.entity.CRefEntity
import mod.iceandshadow3.compat.world.CWorld
import net.minecraft.entity.Entity
import net.minecraft.world.World
import net.minecraftforge.common.util.ITeleporter

class TeleporterDeferred(where: BDimension) extends ITeleporter {
	override def placeEntity(world: World, entity: Entity, yaw: Float): Unit = {
		where.placeVisitor(new CWorld(world), CRefEntity.wrap(entity), yaw: Float)
	}
}
