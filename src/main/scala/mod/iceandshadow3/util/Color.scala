package mod.iceandshadow3.util

class Color(val colorcode: Int) {
	def this(r: Int, g: Int, b: Int) = this((r&0xff) << 16 | (g&0xff) << 8 | (b&0xff))

	def r = colorcode >> 16 & 0xff
	def g = colorcode >> 8 & 0xff
	def b = colorcode & 0xff
	def red: Float = r/255f
	def green: Float = g/255f
	def blue: Float = b/255f
}
object Color {
	val BLACK = new Color(0)
	val RED = new Color(0xff0000)
	val GREEN = new Color(0x00ff00)
	val BLUE = new Color(0x0000ff)
}
