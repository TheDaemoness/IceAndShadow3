package mod.iceandshadow3.lib.compat

import java.nio.file.Path

import com.google.common.base.Charsets
import com.google.gson.JsonObject
import mod.iceandshadow3.{ContentLists, IaS3}
import mod.iceandshadow3.lib.compat.block.impl.{BinderBlock, BinderBlockVar}
import mod.iceandshadow3.lib.compat.client.impl.{AParticleType, BinderParticle}
import mod.iceandshadow3.lib.compat.entity.impl.{BinderEntity, BinderEntityMob}
import mod.iceandshadow3.lib.compat.entity.state.impl.{AStatusEffect, BinderStatusEffect}
import mod.iceandshadow3.lib.compat.item.impl.BinderItem
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
		final val fileGen: BFileGen = new BFileGen("ias3_builtin_recipes") {
			final private val data = ("{\"type\":\""+IaS3.MODID+":builtin\"}").getBytes(Charsets.US_ASCII)
			final protected def getData(root: Path) = {
				import scala.jdk.CollectionConverters._
				val retval = new java.util.HashMap[Path, Array[Byte]]()
				for(rloc <- map.keySet().asScala) {
					val name = rloc.getPath
					retval.put(BFileGen.getDataPath(root, s"recipes/$name.json"), data)
				}
				retval
			}
		}
		//Set registry name in the registerRecipeSerializers bit.
		private val map = new java.util.HashMap[ResourceLocation, String => IRecipe[_]]
		override def read(recipeId: ResourceLocation, json: JsonObject) = map.get(recipeId)(recipeId.getPath)
		override def read(recipeId: ResourceLocation, buffer: PacketBuffer) = map.get(recipeId)(recipeId.getPath)
		override def write(buffer: PacketBuffer, recipe: IRecipe[_]): Unit = {}
		private var frozeRecipes: Boolean = false

		private[compat] def add(loc: ResourceLocation, what: String => IRecipe[_]): Boolean = {
			if(frozeRecipes) {
				IaS3.logger().error("Crafting recipe factory added too late: "+loc.getPath)
				false
			} else if(map.putIfAbsent(loc, what) != null) {
				IaS3.logger().error("ID collision for crafting recipe: "+loc.getPath)
				false
			} else true
		}
		private[iceandshadow3] def freeze(): IRecipeSerializer[_] = {
			frozeRecipes = true
			IaS3.logger().debug(s"Received ${map.size()} builtin recipes")
			this
		}
	}

	private[iceandshadow3] def getFileGen = BuiltinRecipeProxy.fileGen
	private[iceandshadow3] def freeze(): Unit = {
		BinderBlock.freeze()
	}

	private[iceandshadow3] def registerRecipeSerializers(reg: IForgeRegistry[IRecipeSerializer[_]]): Unit = {
		BuiltinRecipeProxy.freeze()
		BuiltinRecipeProxy.setRegistryName(IaS3.MODID, "builtin")
		reg.register(BuiltinRecipeProxy)
	}

	private[iceandshadow3] def registerBlocks(reg: IForgeRegistry[Block]): Unit = {
		for (binding <- BinderBlock) {
			binding._1.setRegistryName(binding._1.namespace, binding._1.modName)
			reg.register(binding._1)
		}
	}

	private[iceandshadow3] def registerItems(reg: IForgeRegistry[Item]): Unit = {
		for (binding <- BinderBlock) {
			if (binding._2 != null) {
				binding._2.setRegistryName(binding._2.namespace, binding._2.modName)
				reg.register(binding._2)
			}
		}
		for (item <- BinderItem.freeze()) {
			item.setRegistryName(item.namespace, item.modName)
			reg.register(item)
		}
		//TODO: Spawn eggs.
	}

	private[iceandshadow3] def registerEntities(reg: IForgeRegistry[EntityType[_ <: Entity]]): Unit = {
		for (amob <- BinderEntityMob) {
			reg.register(amob)
		}
		for (binder <- BinderEntity.binders) {
			if (!binder.frozen) for (et <- binder.freeze()) {
				reg.register(et.asInstanceOf[EntityType[_ <: Entity]])
			}
		}
	}

	private[iceandshadow3] def registerPots(reg: IForgeRegistry[Effect]): Unit = {
		for (fx <- BinderStatusEffect.freeze()) {
			if (fx.isInstanceOf[AStatusEffect]) reg.register(fx)
		}
	}

	private[iceandshadow3] def registerSounds(reg: IForgeRegistry[SoundEvent]): Unit = {
		for(snd <- WSound.freeze()) reg.register(snd)
	}

	private[iceandshadow3] def registerParticles(reg: IForgeRegistry[ParticleType[_ <: IParticleData]]): Unit = {
		for (fx <- BinderParticle.freeze()) fx match {
			case registerable: AParticleType => reg.register(registerable)
			case _ =>
		}
	}

	private[iceandshadow3] def finish(): Unit = {
		BinderBlockVar.freeze()
	}

	def addRecipeCallback(name: String, fn: String => IRecipe[_]) = {
		//Yes, with the methods in ECraftingType, we do end up generating a resource location twice.
		BuiltinRecipeProxy.add(IaS3.rloc(name), fn)
		ContentLists.namesRecipe.add(name)
	}
}
