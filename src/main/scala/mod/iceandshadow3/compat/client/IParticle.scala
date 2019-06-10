package mod.iceandshadow3.compat.client

import mod.iceandshadow3.spatial.IVec3
import mod.iceandshadow3.util.Color

trait IParticle {
	def iasSetColor(color: Color)
	def iasSetAlpha(alpha: Float)
	def iasSetScale(scale: Float)
	def iasSetGravity(gravity: Float)
	def iasSetMotion(what: IVec3)
	def iasSetTexture(mcid: Int)
	def age: Int
}
object IParticle {

}
