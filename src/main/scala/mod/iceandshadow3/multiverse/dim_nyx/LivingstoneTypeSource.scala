package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.lib.util.FnOptions
import mod.iceandshadow3.multiverse.gaia.ELivingstoneType

object LivingstoneTypeSource {
	private def getCommon(rng: Random): ELivingstoneType = rng.nextInt(3) match {
		case 0 => ELivingstoneType.BLUE
		case 1 => ELivingstoneType.CYAN
		case _ => null
	}
	private def getUncommon(rng: Random): ELivingstoneType = rng.nextInt(3) match {
		case 0 => ELivingstoneType.GREEN
		case 1 => ELivingstoneType.WHITE
		case _ => null
	}
	private def getRare(rng: Random): ELivingstoneType = rng.nextInt(3) match {
		case 0 => ELivingstoneType.BROWN
		case 1 => ELivingstoneType.PURPLE
		case _ => null
	}
	private class StoneGen(override protected val default: ELivingstoneType, fns: (Random => ELivingstoneType)*)
	extends FnOptions[Random, ELivingstoneType, ELivingstoneType](fns:_*) {
		override protected def discard(what: ELivingstoneType) = what == null
		override protected def transform(what: ELivingstoneType) = what
	}
	val genCommon: Random => ELivingstoneType =
		new StoneGen(ELivingstoneType.GREEN, getCommon, getCommon)
	val genUncommon: Random => ELivingstoneType =
		new StoneGen(ELivingstoneType.PURPLE, getCommon, getUncommon, getUncommon)
	val genRare: Random => ELivingstoneType =
		new StoneGen(ELivingstoneType.RED, getCommon, getUncommon, getRare, getRare)
	final class Sources(rng: Random) {
		lazy val common = genCommon(rng)
		lazy val uncommon = genUncommon(rng)
		lazy val rare = genRare(rng)
	}
}
