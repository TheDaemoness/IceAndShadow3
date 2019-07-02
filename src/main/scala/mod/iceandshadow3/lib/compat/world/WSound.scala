package mod.iceandshadow3.lib.compat.world

import javax.annotation.Nullable
import mod.iceandshadow3.{ContentLists, IaS3}
import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer
import mod.iceandshadow3.lib.compat.util.CNVCompat
import mod.iceandshadow3.spatial.IPosBlock
import net.minecraft.util.{ResourceLocation, SoundCategory, SoundEvent}
import net.minecraftforge.registries.ForgeRegistries

import scala.collection.mutable.ListBuffer

case class WSound protected(@Nullable private val soundevent: SoundEvent) {
	private[compat] def event: Option[SoundEvent] = Option(soundevent)
	def play(world: TWWorld, place: IPosBlock, volume: Float, freqshift: Float): Unit = {
		if(soundevent == null) return
		world.exposeWorld().playSound(null,
			CNVCompat.toBlockPos(place),
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
		val fullname = s"${domain.name}_$name"
		val location = new ResourceLocation(IaS3.MODID, fullname)
		val soundevent = new SoundEvent(location)
		if(newsounds != null) {
			newsounds += soundevent
			ContentLists.soundname.add(fullname)
			WSound(soundevent)
		} else {
			IaS3.bug(domain, "Attempt add a sound name too late.")
			WSound.silent
		}
	}
	private[iceandshadow3] def freeze(): Iterable[SoundEvent] = {
		val retval = newsounds
		for(sound <- retval) sound.setRegistryName(sound.getName)
		newsounds = null
		retval
	}
	def apply(id: String): WSound =
		WSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(id)))
	val silent: WSound = WSound(null.asInstanceOf[SoundEvent])
}
