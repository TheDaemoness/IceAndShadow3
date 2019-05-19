package mod.iceandshadow3.util

object L3Conversions {
	implicit def fromBool(value: Boolean): L3 = L3.fromBool(value)
	implicit def fromBool(value: java.lang.Boolean): L3 = if(value == null) L3.NULL else L3.fromBool(value.booleanValue())
	implicit def fromInt(value: Int): L3 = L3.fromInt(value)
}
