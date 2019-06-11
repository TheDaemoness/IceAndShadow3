package mod.iceandshadow3.compat.world

import java.util.Collections

import javax.annotation.Nullable
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.compat.entity.WEntityPlayer
import mod.iceandshadow3.spatial.IVec3
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.{ResourceLocation, SoundCategory, SoundEvent}
import net.minecraftforge.registries.{ForgeRegistries, IForgeRegistry}

import scala.collection.JavaConverters._

case class WSound(@Nullable private val soundevent: SoundEvent) {
	private[compat] def event: Option[SoundEvent] = Option(soundevent)
	def play(world: TWWorld, place: IVec3, volume: Float, freqshift: Float): Unit = {
		if(soundevent == null) return
		world.exposeWorld().playSound(null,
			new BlockPos(place.xDouble, place.yDouble, place.zDouble),
			soundevent, SoundCategory.MASTER, volume, freqshift
		)
	}
	def play(who: WEntityPlayer, volume: Float, freqshift: Float): Unit = {
		who.player.playSound(soundevent, volume, freqshift)
	}
}

object WSound {
	private var newsounds: java.util.List[SoundEvent] = new java.util.LinkedList[SoundEvent]
	def addSound(domain: BDomain, name: String): WSound = {
		try {
			val location = new ResourceLocation(IaS3.MODID, s"${domain.name}_$name")
			val soundevent = new SoundEvent(location)
			soundevent.setRegistryName(location)
			newsounds.add(soundevent)
			return WSound(soundevent)
		} catch {
			case e: UnsupportedOperationException => IaS3.bug(e, "Attempt add a sound name too late.")
		}
		WSound(null)
	}
	private[iceandshadow3] def registerSounds(registry: IForgeRegistry[SoundEvent]): Unit = {
		for(soundevent <- newsounds.asScala) registry.register(soundevent)
		newsounds = Collections.emptyList()
	}
	def lookup(id: String): WSound =
		WSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(id)))
}
