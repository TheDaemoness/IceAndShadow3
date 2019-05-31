package mod.iceandshadow3.util

object SMath {
  def bound(min: Int, what: Int, max: Int) = Math.min(max, Math.max(min, what))
  def bound(min: Long, what: Long, max: Long) = Math.min(max, Math.max(min, what))
  def sinelike(input: Double): Double = (1d-Math.cos(input*Math.PI))/2
}