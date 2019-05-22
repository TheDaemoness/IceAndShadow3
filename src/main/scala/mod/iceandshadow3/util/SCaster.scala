package mod.iceandshadow3.util

import scala.reflect.{ClassTag, classTag}

object SCaster {
	// TODO: We can probably write a more complete version using TypeTags.
	def cast[T <: Object: ClassTag](what: Object): Option[T] = try {
		if(what == null) None else Some(classTag[T].runtimeClass.cast(what).asInstanceOf[T])
	} catch {
		case c: ClassCastException => None
	}
}