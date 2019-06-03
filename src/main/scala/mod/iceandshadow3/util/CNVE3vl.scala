package mod.iceandshadow3.util

object CNVE3vl {
	implicit def fromBool(value: Boolean): E3vl = E3vl.fromBool(value)
	implicit def fromBool(value: java.lang.Boolean): E3vl = E3vl.fromBoolBoxed(value)
	implicit def fromInt(value: Int): E3vl = E3vl.fromInt(value)
}
