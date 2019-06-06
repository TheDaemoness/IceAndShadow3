package mod.iceandshadow3.damage

case class AdsArmorValue(hard: Float, soft: Float) {
	private val divisor = (hard+soft)/2f
	def apply(dmg: Float) = {
		val basereduced = Math.max(dmg-hard, 0)
		if(divisor >= 0) basereduced/(1+divisor) else basereduced*(1-divisor/2)
	}
}
object AdsArmorValue {
	val NONE = AdsArmorValue(0f, 0f)
	val ABSOLUTE = AdsArmorValue(Float.MaxValue, 0f)
}
