package mod.iceandshadow3.util

import scala.collection.JavaConverters._

//TODO: Quick-n-dirty implementation that will need polishing later.
class StringMap[T] extends Iterable[(String, T)] with java.util.Map[String, T] {
	private val map = new java.util.HashMap[String, T]
	def iterator = map.asScala.iterator
	def clear() = map.clear()
	def containsKey(key: Any): Boolean = map.containsKey(key)
	def containsValue(value: Any): Boolean = map.containsValue(value)
	def entrySet(): java.util.Set[java.util.Map.Entry[String,T]] = map.entrySet
	def get(key: Any): T = map.get(key)
	def keySet(): java.util.Set[String] = map.keySet
	def put(key: String, value: T): T = map.put(key, value)
	def putAll(other: java.util.Map[_ <: String, _ <: T]): Unit = map.putAll(other)
	def remove(key: Any): T = map.remove(key)
	def values(): java.util.Collection[T] = map.values
	override def toString = map.toString
}