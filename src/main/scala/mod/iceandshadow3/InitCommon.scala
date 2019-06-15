package mod.iceandshadow3

import mod.iceandshadow3.compat.block.impl.ABlock
import mod.iceandshadow3.compat.block.impl.BinderBlock
import mod.iceandshadow3.compat.client.impl.{AParticleType, BinderParticle}
import mod.iceandshadow3.compat.entity.impl.{AMob, BBinderEntity, BinderMob}
import mod.iceandshadow3.compat.entity.state.impl.{AStatusEffect, BinderStatusEffect}
import mod.iceandshadow3.compat.item.impl.AItemBlock
import mod.iceandshadow3.compat.item.impl.BinderItem
import mod.iceandshadow3.compat.world.WSound
import mod.iceandshadow3.compat.world.impl.AModDimension
import mod.iceandshadow3.forge.{EventFisherman, Teleporter}
import mod.iceandshadow3.world._
import net.minecraft.block.Block
import net.minecraft.entity.{Entity, EntityType}
import net.minecraft.item.Item
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType
import net.minecraft.potion.Effect
import net.minecraft.server.MinecraftServer
import net.minecraft.util.SoundEvent
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.ModDimension
import net.minecraftforge.registries.IForgeRegistry

private[iceandshadow3] object InitCommon {
	private val domains = List(DomainAlien, DomainNyx, DomainGaia)
	private val dimensionsWrapped = List(new AModDimension(DimensionNyx))
	def populateBinders(): Unit = {
		BinderStatusEffect.populate()
		BinderParticle.populate()
	}

	// ---

	private var blockBindings: Array[Array[(ABlock, AItemBlock)]] = _
	private var mobs: Array[EntityType[_ <: AMob]] = _

	def initEarly(): Unit = {
		for (domain <- domains) {domain.initEarly()}
		blockBindings = BinderBlock.freeze()
		mobs = BinderMob.freeze()
	}

	def registerBlocks(reg: IForgeRegistry[Block]): Unit = for (bindings <- blockBindings) {
		for (binding <- bindings) reg.register(binding._1)
	}

	def registerItems(reg: IForgeRegistry[Item]): Unit = {
		for (items <- BinderItem.freeze()) {
			for (item <- items) {reg.register(item)}
		}
		for (bindings <- blockBindings) {
			for (binding <- bindings) {
				if (binding._2 != null) reg.register(binding._2)
			}
		}
		//TODO: Spawn eggs.
	}

	def registerEntities(reg: IForgeRegistry[EntityType[_ <: Entity]]): Unit = {
		for (amob <- mobs) {
			reg.register(amob)
		}
		for (binder <- BBinderEntity.binders) {
			if (!binder.frozen) for (et <- binder.freeze()) {
				reg.register(et.asInstanceOf[EntityType[_ <: Entity]])
			}
		}
	}

	def registerDimensions(reg: IForgeRegistry[ModDimension]): Unit = {
		for (dim <- dimensionsWrapped) reg.register(dim)
	}

	def registerBiomes(reg: IForgeRegistry[Biome]): Unit = {
		for (dim <- dimensionsWrapped) reg.register(dim.dimbiome)
	}

	def registerPots(reg: IForgeRegistry[Effect]): Unit = {
		for (fx <- BinderStatusEffect.freeze()) {
			if (fx.isInstanceOf[AStatusEffect]) reg.register(fx)
		}
	}

	def registerSounds(reg: IForgeRegistry[SoundEvent]): Unit = {
		for(snd <- WSound.freeze()) reg.register(snd)
	}

	def registerParticles(reg: IForgeRegistry[ParticleType[_ <: IParticleData]]): Unit = {
		for (fx <- BinderParticle.freeze()) fx match {
			case registerable: AParticleType => reg.register(registerable)
			case _ =>
		}
	}

	def initLate(): Unit = {
		Teleporter.registerSelf()
		EventFisherman.baitHooks()
		for (domain <- domains) domain.initLate()
	}

	def initFinalClient(): Unit = {
		InitClient.finishParticles()
	}
	def initFinalServer(): Unit = {
		BinderParticle.freeze()
	}

	def enableDimensions(): Unit = {
		for (dim <- dimensionsWrapped) dim.enable()
	}

	def primeDimensions(server: MinecraftServer): Unit = {
		//TODO: ALL OF THIS IS A TEMPORARY HACK. COME UP WITH SOMETHING BETTER!
		class RestartRequired extends RuntimeException("Please restart the dedicated server.") {}
		var foundOne = false
		for (dim <- dimensionsWrapped) {
			val iasdim = dim.getIaSDimension
			if (iasdim.coord == null) {
				dim.enable()
				foundOne = true
			}
		}
		if (foundOne) throw new RestartRequired
	}
}