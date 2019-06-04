package mod.iceandshadow3.util

object SMath {
  def bound(min: Int, what: Int, max: Int) = Math.min(max, Math.max(min, what))
  def bound(min: Long, what: Long, max: Long) = Math.min(max, Math.max(min, what))
  def sinelike(input: Double): Double = (1d-Math.cos(input*Math.PI))/2
  def interpolate(a: Double, b: Double, fraction: Double, fn: Double => Double) = {
    val amount = fn(fraction)
    a*(1-amount) + b*amount
  }
  def attenuateBetween(lower: Int, where: Int, upper: Int): Double = {
    if(where <= lower) 1d
    else if(where >= upper) 0d
    else (where-lower)/(upper-lower).toDouble
  }
}