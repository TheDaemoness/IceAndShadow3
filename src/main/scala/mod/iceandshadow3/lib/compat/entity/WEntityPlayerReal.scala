package mod.iceandshadow3.lib.compat.entity

import javax.annotation.Nullable
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.LogicDimension
import mod.iceandshadow3.lib.compat.world.{WDimensionCoord, WWorld}
import mod.iceandshadow3.lib.spatial.IVec3
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation

import scala.jdk.CollectionConverters._

class WEntityPlayerReal protected[entity](protected[compat] val spe: ServerPlayerEntity) extends WEntityPlayer(spe) {
	override def advancement(name: String, criteria: String*): Unit = {
		val what = spe.getServer.getAdvancementManager.getAdvancement(new ResourceLocation(IaS3.MODID, name))
		if(what == null) {
			IaS3.logger.error(s"Advancement with id $name does not exist.")
			return
		}
		val advancements = spe.getAdvancements
		for(critname <- what.getCriteria.keySet().asScala) {
			if(criteria.isEmpty || criteria.contains(name)) advancements.grantCriterion(what, critname)
		}
	}

	def teleport(dim: WDimensionCoord, @Nullable placer: WWorld => IVec3): Unit = {
		if(isServerSide) {
			if(!WDimensionCoord.isVoid(dim)) {
				val server = spe.getServer.getWorld(dim.dimtype)
					if(placer != null) {
						val where = placer(new WWorld(server))
						spe.teleport(server, where.xDouble, where.yDouble, where.zDouble, spe.rotationYaw, spe.rotationPitch)
					} else spe.changeDimension(dim.dimtype)
			}
		}
	}

	def teleport(dim: LogicDimension): Unit = teleport(dim.coord, dim.defaultPlace)
}
