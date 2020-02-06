package mod.iceandshadow3.lib.compat.entity

import javax.annotation.Nullable
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.LogicDimension
import mod.iceandshadow3.lib.compat.world.{WDimensionCoord, WWorld}
import mod.iceandshadow3.lib.spatial.IVec3
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

import scala.jdk.CollectionConverters._

class WEntityPlayerFull protected[entity](override protected[compat] val expose: ServerPlayerEntity)
extends WEntityPlayer(expose) {
	override def advancement(name: String, criteria: String*): Unit = {
		val what = expose.getServer.getAdvancementManager.getAdvancement(new ResourceLocation(IaS3.MODID, name))
		if(what == null) {
			IaS3.logger.error(s"Advancement with id $name does not exist.")
			return
		}
		val advancements = expose.getAdvancements
		for(critname <- what.getCriteria.keySet().asScala) {
			if(criteria.isEmpty || criteria.contains(name)) advancements.grantCriterion(what, critname)
		}
	}

	def teleport(dim: WDimensionCoord, @Nullable placer: WWorld => IVec3): Unit = {
		if(isServerSide) {
			if(!WDimensionCoord.isVoid(dim)) {
				val server = expose.getServer.getWorld(dim.dimtype)
					if(placer != null) {
						val where = placer(new WWorld(server))
						expose.teleport(server, where.xDouble, where.yDouble, where.zDouble, expose.rotationYaw, expose.rotationPitch)
					} else expose.changeDimension(dim.dimtype)
			}
		}
	}

	def teleport(dim: LogicDimension): Unit = teleport(dim.coord, dim.defaultPlace)

	override protected[compat] def damageItem(is: ItemStack, amount: Int): Unit = {
		is.damageItem(amount, expose, (spe: ServerPlayerEntity) => spe.sendBreakAnimation(is.getEquipmentSlot))
	}
}
