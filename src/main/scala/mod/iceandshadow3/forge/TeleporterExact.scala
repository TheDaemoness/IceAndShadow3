package mod.iceandshadow3.forge

import mod.iceandshadow3.compat.entity.{CNVEntity, WEntity}
import mod.iceandshadow3.spatial.IVec3
import net.minecraft.entity.Entity
import net.minecraft.world.World
import net.minecraftforge.common.util.ITeleporter

class TeleporterExact(where: IVec3) extends ITeleporter {
	override def placeEntity(world: World, entity: Entity, yaw: Float) = {
		CNVEntity.wrap(entity).teleport(where)
	}
}
