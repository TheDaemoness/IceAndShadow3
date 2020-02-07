package mod.iceandshadow3.lib.util

import java.util.Random

import scala.reflect.{ClassTag, classTag}

object GeneralUtils {
	def cast[T <: Object: ClassTag](what: Object): Option[T] = try {
		// TODO: We can probably write a more complete version using TypeTags.
		if(what == null) None else Some(classTag[T].runtimeClass.cast(what).asInstanceOf[T])
	} catch {
		case c: ClassCastException => None
	}

	def randomPick[T](options: Seq[T], rng: Random): T = options(rng.nextInt(options.size))

	def join(name: String, suffix: String, sep: String = "_") = if(suffix.isEmpty) name else s"$name$sep$suffix"
}