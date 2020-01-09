package mod.iceandshadow3.lib.util

import scala.util.Random

object MathUtils {
  def fastMag(xAbs: Int, zAbs: Int) = Math.max(xAbs, zAbs)+(Math.min(xAbs,zAbs)>>1)
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

  def isInRadius(radius: Int, dims: Int*): Boolean = {
    var sum = 0
    var i = 0
    while(i < dims.size) {
      val what = dims(i)
      sum += what*what
      i += 1
    }
    sum < radius*radius
  }

  def roundRandom(float: Float, rng: Random): Int = {
    val floored = float.intValue()
    val chance = float - floored.toFloat
    if(rng.nextFloat() <= chance) floored+1 else floored
  }
}