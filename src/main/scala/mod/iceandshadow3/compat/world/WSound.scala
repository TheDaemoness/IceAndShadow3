package mod.iceandshadow3.compat.world

import javax.annotation.Nullable
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.compat.entity.WEntityPlayer
import mod.iceandshadow3.spatial.IVec3
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{ResourceLocation, SoundCategory, SoundEvent}
import net.minecraftforge.registries.ForgeRegistries

import scala.collection.mutable.ListBuffer

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
	private var newsounds = new ListBuffer[SoundEvent]
	def addSound(domain: BDomain, name: String): WSound = {
		val location = new ResourceLocation(IaS3.MODID, s"${domain.name}_$name")
		val soundevent = new SoundEvent(location)
		if(newsounds != null) {
			newsounds += soundevent
			soundevent.setRegistryName(location)
			WSound(soundevent)
		} else {
			IaS3.bug(domain, "Attempt add a sound name too late.")
			WSound(null)
		}
	}
	private[iceandshadow3] def freeze(): Iterable[SoundEvent] = {
		val retval = newsounds
		newsounds = null
		retval
	}
	def lookup(id: String): WSound =
		WSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(id)))
}
