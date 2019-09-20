package mod.iceandshadow3.lib.util

import java.util.Random

object CollectUtils {
	def randomPick[T](options: Seq[T], rng: Random): T = options(rng.nextInt(options.size))
}
