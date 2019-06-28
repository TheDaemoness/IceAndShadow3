package mod.iceandshadow3.lib.compat

import mod.iceandshadow3.lib.compat.block.impl.{ABlock, BinderBlock}
import mod.iceandshadow3.lib.compat.client.impl.{AParticleType, BinderParticle}
import mod.iceandshadow3.lib.compat.entity.impl.{AMob, BBinderEntity, BinderMob}
import mod.iceandshadow3.lib.compat.entity.state.impl.{AStatusEffect, BinderStatusEffect}
import mod.iceandshadow3.lib.compat.item.impl.{AItemBlock, BinderItem}
import mod.iceandshadow3.lib.compat.world.WSound
import net.minecraft.block.Block
import net.minecraft.entity.{Entity, EntityType}
import net.minecraft.item.Item
import net.minecraft.particles.{IParticleData, ParticleType}
import net.minecraft.potion.Effect
import net.minecraft.util.SoundEvent
import net.minecraftforge.registries.IForgeRegistry

object Registrar {
	lazy val blockBindings: Array[Array[(ABlock, AItemBlock)]] = BinderBlock.freeze()
	lazy val mobs: Array[EntityType[_ <: AMob]] = BinderMob.freeze()

	def registerBlocks(reg: IForgeRegistry[Block]): Unit = for (bindings <- blockBindings) {
		for (binding <- bindings) reg.register(binding._1)
	}

	def registerItems(reg: IForgeRegistry[Item]): Unit = {
		for (bindings <- blockBindings) {
			for (binding <- bindings) {
				if (binding._2 != null) reg.register(binding._2)
			}
		}
		for (items <- BinderItem.freeze()) {
			for (item <- items) {reg.register(item)}
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
}
