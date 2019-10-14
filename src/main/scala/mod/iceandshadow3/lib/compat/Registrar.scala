package mod.iceandshadow3.lib.compat

import com.google.gson.JsonObject
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.block.impl.{ABlock, BinderBlock, BinderBlockVar}
import mod.iceandshadow3.lib.compat.client.impl.{AParticleType, BinderParticle}
import mod.iceandshadow3.lib.compat.entity.impl.{AMob, BinderEntity, BinderEntityMob}
import mod.iceandshadow3.lib.compat.entity.state.impl.{AStatusEffect, BinderStatusEffect}
import mod.iceandshadow3.lib.compat.item.impl.{AItemBlock, BinderItem}
import mod.iceandshadow3.lib.compat.world.WSound
import net.minecraft.block.Block
import net.minecraft.entity.{Entity, EntityType}
import net.minecraft.item.Item
import net.minecraft.item.crafting.{IRecipe, IRecipeSerializer}
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.{IParticleData, ParticleType}
import net.minecraft.potion.Effect
import net.minecraft.util.{ResourceLocation, SoundEvent}
import net.minecraftforge.registries.IForgeRegistry

object Registrar {
	private[compat] object BuiltinRecipeProxy
	extends net.minecraftforge.registries.ForgeRegistryEntry[IRecipeSerializer[_]]
	with IRecipeSerializer[IRecipe[_]] {
		this.setRegistryName(IaS3.MODID, "builtin")
		private val map = new java.util.HashMap[ResourceLocation, IRecipe[_]]
		override def read(recipeId: ResourceLocation, json: JsonObject) = map.get(recipeId)
		override def read(recipeId: ResourceLocation, buffer: PacketBuffer) = map.get(recipeId)
		override def write(buffer: PacketBuffer, recipe: IRecipe[_]): Unit = {}
		private var frozeRecipes: Boolean = false

		def add(what: IRecipe[_]): Boolean = {
			if(frozeRecipes) {
				IaS3.logger().error("Crafting recipe added too late: "+what.getId)
				false
			} else if(map.putIfAbsent(what.getId, what) != null) {
				IaS3.logger().error("ID collision for crafting recipe: "+what.getId)
				false
			} else true
		}
		def freeze(): IRecipeSerializer[_] = {
			frozeRecipes = true
			this
		}
	}

	lazy val blockBindings: Array[Array[(ABlock, AItemBlock)]] = BinderBlock.freeze()
	lazy val mobs: Array[EntityType[_ <: AMob]] = BinderEntityMob.freeze()

	def registerRecipeLoaders(reg: IForgeRegistry[IRecipeSerializer[_]]): Unit = {
		reg.register(BuiltinRecipeProxy.freeze())
	}

	def registerBlocks(reg: IForgeRegistry[Block]): Unit = {
		for (bindings <- blockBindings) {
			for (binding <- bindings) reg.register(binding._1)
		}
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
		for (binder <- BinderEntity.binders) {
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

	def finish(): Unit = {
		BinderBlockVar.freeze()
	}

	private[compat] def addRecipe(what: IRecipe[_]): Unit = BuiltinRecipeProxy.add(what)
}
