package mod.iceandshadow3.lib.util

import javax.annotation.Nullable

import scala.language.implicitConversions

object CNVE3vl {
	implicit def fromBool(value: Boolean): E3vl = E3vl.fromBool(value)
	implicit def fromBool(@Nullable value: java.lang.Boolean): E3vl = E3vl.fromBoolBoxed(value)
	implicit def fromInt(value: Int): E3vl = E3vl.fromInt(value)
}
