package mod.iceandshadow3.forge

import mod.iceandshadow3.compat.CNVVec3
import mod.iceandshadow3.compat.entity.CNVEntity
import mod.iceandshadow3.compat.world.impl.AModDimension
import mod.iceandshadow3.compat.world.{WDimensionCoord, WWorld}
import mod.iceandshadow3.spatial.IVec3
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.entity.Entity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.play.server.{SPlayEntityEffectPacket, SPlaySoundEventPacket, SPlayerAbilitiesPacket, SRespawnPacket, SServerDifficultyPacket}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ServerWorld
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent
import net.minecraftforge.eventbus.api.{EventPriority, SubscribeEvent}

/** Actual handler for teleports to/from IaS3 dimensions.
	* Here's hoping your entities don't override changeDimension.
	*/
object Teleporter {
	def registerSelf(): Unit = {
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, onTeleportFrom)
		MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, onTeleportTo)
	}
	@SubscribeEvent
	def onTeleportFrom(event: EntityTravelToDimensionEvent): Unit = {
		val from = event.getEntity.dimension
		val to = event.getDimension
		val iasfrom = AModDimension.lookup(from.getModType)
		val traveler = event.getEntity
		if(iasfrom != null) {
			if(!iasfrom.handleEscape(CNVEntity.wrap(traveler), WDimensionCoord(to))) {
				event.setCanceled(true)
				return
			} else if(to.isVanilla) {
				//Hack to allow end teleports to work (and overworld teleports to be neater).
				val worldto = traveler.getServer.getWorld(to)
				val spawn = worldto.getSpawnCoordinate
				if(spawn != null) event.getEntity.moveToBlockPosAndAngles(spawn, traveler.rotationYaw, traveler.rotationPitch)
			}
		}
	}
	@SubscribeEvent
	def onTeleportTo(event: EntityTravelToDimensionEvent): Unit = {
		val to = event.getDimension
		val iasto = AModDimension.lookup(to.getModType)
		if(iasto != null) {
			event.setCanceled(true)
			val traveler = event.getEntity
			val from = traveler.dimension
			val server = traveler.getServer
			val worldfrom = server.getWorld(from)
			val worldto = server.getWorld(to)
			val where = iasto.handleArrival(new WWorld(worldto), CNVEntity.wrap(traveler))
			if(where != null) traveler match {
				case player: ServerPlayerEntity => teleportPlayer(
					player, from, to, worldfrom, worldto, where
				)
				case _ => teleportEntity(traveler, from, to, worldfrom, worldto, where)
			}
		}
	}

	def teleportEntity(
		traveler: Entity,
		from: DimensionType, to: DimensionType,
		worldFrom: ServerWorld, worldTo: ServerWorld, where: IVec3
	): Unit = {
		traveler.dimension = to
		traveler.func_213319_R() //Dismount everyone.
		val newcomer = traveler.getClass.cast(traveler.getType.create(worldTo))
		if (newcomer != null) {
			newcomer.copyDataFromOld(traveler)
			newcomer.moveToBlockPosAndAngles(CNVVec3.toBlockPos(where), newcomer.rotationYaw, newcomer.rotationPitch)
			newcomer.setMotion(traveler.getMotion)
			worldTo.func_217460_e(newcomer)
		}
		worldFrom.resetUpdateEntityTick()
		worldTo.resetUpdateEntityTick()
	}

	def teleportPlayer(
		p: ServerPlayerEntity,
		from: DimensionType, to: DimensionType,
		worldFrom: ServerWorld, worldTo: ServerWorld, where: IVec3
	): Unit = {
		p.dimension = to
		val worldinfo = p.world.getWorldInfo
		p.connection.sendPacket(new SRespawnPacket(to, worldinfo.getGenerator, p.interactionManager.getGameType))
		p.connection.sendPacket(new SServerDifficultyPacket(worldinfo.getDifficulty, worldinfo.isDifficultyLocked))
		val playerlist = p.server.getPlayerList
		playerlist.updatePermissionLevel(p)
		worldFrom.removeEntity(p, true)
		p.revive()
		p.setWorld(worldTo)
		worldTo.func_217447_b(p)
		CriteriaTriggers.CHANGED_DIMENSION.trigger(p, from, to)
		p.moveToBlockPosAndAngles(CNVVec3.toBlockPos(where), p.rotationYaw, p.rotationPitch)
		p.connection.setPlayerLocation(p.posX, p.posY, p.posZ, p.rotationYaw, p.rotationPitch)
		p.interactionManager.setWorld(worldTo)
		p.connection.sendPacket(new SPlayerAbilitiesPacket(p.playerAbilities))
		playerlist.sendWorldInfo(p, worldTo)
		playerlist.sendInventory(p)
		import scala.collection.JavaConverters._
		for (effect <- p.getActivePotionEffects.asScala) p.connection.sendPacket(
			new SPlayEntityEffectPacket(p.getEntityId, effect)
		)
		//;_; There's no good way to override this sound effect. Those numbers are mapped via switch statement.
		//We can disable it, but that makes the teleport feel off.
		p.connection.sendPacket(
			new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false)
		)
		//Also the variables for last Experience, Heath, and FoodLevel are private.
		net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerChangedDimensionEvent(p, from, to)
	}
}
