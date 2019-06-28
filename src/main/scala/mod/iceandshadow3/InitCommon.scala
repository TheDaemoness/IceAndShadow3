package mod.iceandshadow3

import mod.iceandshadow3.lib.compat.Binders
import mod.iceandshadow3.lib.compat.client.impl.BinderParticle
import mod.iceandshadow3.lib.compat.entity.state.impl.BinderStatusEffect
import mod.iceandshadow3.lib.compat.world.impl.AModDimension
import mod.iceandshadow3.lib.forge.{EventFisherman, Teleporter}
import mod.iceandshadow3.multiverse._
import mod.iceandshadow3.multiverse.misc.Statuses
import net.minecraft.server.MinecraftServer
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.ModDimension
import net.minecraftforge.registries.IForgeRegistry

private[iceandshadow3] object InitCommon {
	private val domains = List(DomainAlien, DomainGaia, DomainNyx)
	private lazy val dimensionsWrapped = List(new AModDimension(DimensionNyx))

	// ---

	/** Called before initEarly during Forge initialization. */
	def initNormalNode(): Unit = {
		Binders.prepopulate()
	}
	/** Called before initEarly during tool mode initialization. */
	def initToolMode(): Unit = {
		Statuses.init()
	}

	def initEarly(): Unit = {
		for (domain <- domains) {domain.initEarly()}
	}

	def registerDimensions(reg: IForgeRegistry[ModDimension]): Unit = {
		for (dim <- dimensionsWrapped) reg.register(dim)
	}

	def registerBiomes(reg: IForgeRegistry[Biome]): Unit = {
		for (dim <- dimensionsWrapped) reg.register(dim.dimbiome)
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