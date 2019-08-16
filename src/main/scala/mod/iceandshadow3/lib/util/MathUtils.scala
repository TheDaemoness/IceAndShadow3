package mod.iceandshadow3.lib.util

object MathUtils {
  def bound(min: Int, what: Int, max: Int) = Math.min(max, Math.max(min, what))
  def bound(min: Long, what: Long, max: Long) = Math.min(max, Math.max(min, what))
  def sinelike(input: Double): Double = (1d-Math.cos(input*Math.PI))/2
  def interpolate(a: Double, fraction: Double, b: Double) = {
    a*(1-fraction) + b*fraction
  }

  /** Returns 1 below the lower argument, 0 above the higher, and interpolates linearly between.
    */
  def ratioBelow(lower: Int, where: Int, upper: Int): Float = {
    if(where <= lower) 1f
    else if(where >= upper) 0f
    else 1f-((where-lower).toFloat/(upper-lower))
  }
}