package mod.iceandshadow3.compat.world

import java.util.Collections

import javax.annotation.Nullable
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.spatial.IVec3
import net.minecraft.util.{ResourceLocation, SoundCategory, SoundEvent}
import net.minecraftforge.registries.{ForgeRegistries, IForgeRegistry}

import scala.collection.JavaConverters._

case class CSound(@Nullable private val soundevent: SoundEvent) {
	private[compat] def event: Option[SoundEvent] = Option(soundevent)
	def play(world: TCWorld, place: IVec3, volume: Float, freqshift: Float): Unit = {
		if(soundevent == null) return
		world.exposeWorld().playSound(null,
			place.xDouble, place.yDouble, place.zDouble,
			soundevent, SoundCategory.MASTER, volume, freqshift
		)
	}
}

object CSound {
	private var newsounds: java.util.List[SoundEvent] = new java.util.LinkedList[SoundEvent]
	def addSound(domain: BDomain, name: String): CSound = {
		try {
			val location = new ResourceLocation(IaS3.MODID, s"${domain.name}_$name")
			val soundevent = new SoundEvent(location)
			soundevent.setRegistryName(location)
			newsounds.add(soundevent)
			return CSound(soundevent)
		} catch {
			case e: UnsupportedOperationException => IaS3.bug(e, "Attempt add a sound name too late.")
		}
		CSound(null)
	}
	private[iceandshadow3] def registerSounds(registry: IForgeRegistry[SoundEvent]): Unit = {
		for(soundevent <- newsounds.asScala) registry.register(soundevent)
		newsounds = Collections.emptyList()
	}
	implicit def lookup(id: String): CSound =
		CSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(id)))
}
