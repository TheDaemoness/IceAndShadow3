package mod.iceandshadow3.lib.compat.client

import mod.iceandshadow3.spatial.IVec3
import mod.iceandshadow3.util.Color

trait IParticle {
	def iasSetColor(color: Color): Unit
	def iasSetAlpha(alpha: Float): Unit
	def iasSetScale(scale: Float): Unit
	def iasSetGravity(gravity: Float): Unit
	def iasSetMotion(what: IVec3): Unit
	def iasSetTexture(mcid: Int): Unit
	def age: Int
}
object IParticle {

}
