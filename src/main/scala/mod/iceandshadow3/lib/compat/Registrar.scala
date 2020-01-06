package mod.iceandshadow3.lib.compat

import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

import com.google.common.base.Charsets
import com.google.common.cache.{CacheBuilder, CacheLoader}
import com.google.gson.JsonObject
import javax.annotation.Nullable
import mod.iceandshadow3.{ContentLists, IaS3}
import mod.iceandshadow3.lib.compat.block.impl.{BinderBlock, BinderBlockVar, TileEntities}
import mod.iceandshadow3.lib.compat.client.impl.{AParticleType, BinderParticle}
import mod.iceandshadow3.lib.compat.entity.impl.{BinderEntity, BinderEntityMob}
import mod.iceandshadow3.lib.compat.entity.state.impl.{AStatusEffect, BinderStatusEffect}
import mod.iceandshadow3.lib.compat.item.impl.BinderItem
import mod.iceandshadow3.lib.compat.recipe.{AddedRecipesInfo, RecipeFactory}
import mod.iceandshadow3.lib.compat.world.WSound
import net.minecraft.block.Block
import net.minecraft.entity.{Entity, EntityType}
import net.minecraft.item.Item
import net.minecraft.item.crafting.{IRecipe, IRecipeSerializer}
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.{IParticleData, ParticleType}
import net.minecraft.potion.Effect
import net.minecraft.tileentity.{TileEntity, TileEntityType}
import net.minecraft.util.{ResourceLocation, SoundEvent}
import net.minecraftforge.registries.IForgeRegistry

object Registrar {
	private[compat] object RecipeHandler
	extends net.minecraftforge.registries.ForgeRegistryEntry[IRecipeSerializer[_]]
	with IRecipeSerializer[IRecipe[_]] {
		final val fileGen: BFileGen = new BFileGen("ias3_builtin_recipes") {
			final private val recipeStub = ("{\"type\":\""+IaS3.MODID+":builtin\"}").getBytes(Charsets.US_ASCII)
			final protected def getData(root: Path) = {
				val retval = new java.util.HashMap[Path, Array[Byte]]()
				val subroot = BFileGen.getDataPath(root)
				for((id, factory) <- factories) {
					retval.put(subroot.resolve(s"recipes/${id.name}.json"), recipeStub)
					for(advancement <- factory.advancement; json <- advancement._2) retval.putIfAbsent(
						subroot.resolve(s"advancements/recipes/${advancement._1}.json"),
						json.toString.getBytes(Charsets.US_ASCII)
					)
				}
				retval
			}
		}
		@Nullable private var factories = new scala.collection.mutable.HashMap[WId, RecipeFactory]
		private val forRecipeGen = new ConcurrentHashMap[ResourceLocation, RecipeFactory]
		private lazy val activeCache = CacheBuilder.newBuilder().build(new CacheLoader[ResourceLocation, IRecipe[_]] {
				override def load(key: ResourceLocation) = {
					val retval = forRecipeGen.remove(key)
					if(retval != null) retval.build else null
				}
			})
		override def read(recipeId: ResourceLocation, json: JsonObject) = activeCache.get(recipeId)
		override def read(recipeId: ResourceLocation, buffer: PacketBuffer) = activeCache.get(recipeId)
		override def write(buffer: PacketBuffer, recipe: IRecipe[_]): Unit = {}

		def freeze(): AddedRecipesInfo = {
			for((id, factory) <- factories) forRecipeGen.put(id.asVanilla, factory)
			val retval = new AddedRecipesInfo(factories)
			factories = null
			IaS3.logger().debug(s"RecipeHandler frozen with ${retval.size} recipes")
			retval
		}

		def add(what: RecipeFactory): Boolean = {
			if(factories == null) {
				IaS3.bug(new IllegalStateException(s"Attempted to add $what after the recipe handler was frozen"))
				false
			} else {
				val present = factories.getOrElseUpdate(what.id, what)
				if (present != what) {
					IaS3.bug(new IllegalArgumentException(s"Recipe ID collision for ${present.id}"))
					false
				} else true
			}
		}

		@Nullable def info: AddedRecipesInfo = if(factories == null) null else new AddedRecipesInfo(factories)
	}

	private[iceandshadow3] def getFileGen = RecipeHandler.fileGen
	private[iceandshadow3] def freeze(): Unit = {
		BinderBlock.freeze()
	}

	private[iceandshadow3] def registerRecipeSerializers(reg: IForgeRegistry[IRecipeSerializer[_]]): Unit = {
		RecipeHandler.setRegistryName(IaS3.MODID, "builtin")
		reg.register(RecipeHandler)
	}

	private[iceandshadow3] def registerBlocks(reg: IForgeRegistry[Block]): Unit = {
		for (binding <- BinderBlock) {
			binding._1.setRegistryName(binding._1.id.asVanilla)
			reg.register(binding._1)
		}
	}

	private[iceandshadow3] def registerItems(reg: IForgeRegistry[Item]): Unit = {
		for (binding <- BinderBlock) {
			if (binding._2 != null) {
				binding._2.setRegistryName(binding._2.id.asVanilla)
				reg.register(binding._2)
			}
		}
		for (item <- BinderItem.freeze()) {
			item.setRegistryName(item.id.asVanilla)
			reg.register(item)
		}
		//TODO: Spawn eggs.
	}

	private[iceandshadow3] def registerTileEntities(reg: IForgeRegistry[TileEntityType[_ <: TileEntity]]): Unit = {
		import scala.jdk.CollectionConverters._
		TileEntities.collect(ContentLists.block.iterator().asScala).foreach(reg.register)
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
		RecipeHandler.freeze()
	}

	private[compat] def addRecipeFactory(factory: RecipeFactory): Boolean = RecipeHandler.add(factory)
	@Nullable def recipeInfo = RecipeHandler.info
}
