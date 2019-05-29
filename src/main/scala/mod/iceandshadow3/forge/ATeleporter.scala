package mod.iceandshadow3.forge

import mod.iceandshadow3.compat.Vec3Conversions
import mod.iceandshadow3.compat.entity.CRefEntity
import mod.iceandshadow3.spatial.IVec3
import net.minecraft.entity.Entity
import net.minecraft.world.World
import net.minecraftforge.common.util.ITeleporter

class ATeleporter(where: IVec3) extends ITeleporter {
	override def placeEntity(world: World, entity: Entity, yaw: Float) = {
		CRefEntity.wrap(entity).teleport(where)
	}
}
