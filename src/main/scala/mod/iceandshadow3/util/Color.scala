package mod.iceandshadow3.util

/** Color class. Matches the byte order of Minecraft colors (0xRRGGBB) with no alpha.
	* If alpha is added, alpha will be premultiplied and available separately.
	*/
case class Color(colorcode: Int) {
	def this(r: Int, g: Int, b: Int) = this((r&0xff) << 16 | (g&0xff) << 8 | (b&0xff))
	def this(r: Float, g: Float, b: Float) = this((r*0xff).toInt << 16 | (g*0xff).toInt << 8 | (b*0xff).toInt)
	def r = colorcode >> 16 & 0xff
	def g = colorcode >> 8 & 0xff
	def b = colorcode & 0xff
	def red: Float = r/255f
	def green: Float = g/255f
	def blue: Float = b/255f
	def multiply(scalar: Float): Color = new Color(red*scalar, green*scalar, blue*scalar)
}
object Color {
	val BLACK = new Color(0)
	val WHITE = new Color(0xffffff)
	val RED = new Color(0xff0000)
	val YELLOW = new Color(0xffff00)
	val GREEN = new Color(0x00ff00)
	val CYAN = new Color(0x00ffff)
	val BLUE = new Color(0x0000ff)
	val MAGENTA = new Color(0xff00ff)
}
