package mod.iceandshadow3.util

import scala.collection.JavaConverters._
import scala.collection.mutable

class StringMap[T] extends mutable.Map[String, T] with TQuietFailing {
	//TODO: Quick-n-dirty implementation that will need polishing later, likely conversion to a trie.
	private val map = new java.util.HashMap[String, T]

	def isValidKey(key: String): Boolean = true
	def iterator = map.asScala.iterator

	/** Inserts a key-value pair if the key is valid. */
	override def +=(kv: (String, T)) = {
		val canput = isValidKey(kv._1)
		if(canput) map.put(kv._1, kv._2)
		setFailure(!canput, new IllegalArgumentException(s"Invalid key for $this: ${kv._1}"))
		this
	}
	override def -=(key: String) = {map.remove(key); this}
	override def get(key: String) = Option(map.getOrDefault(key, null.asInstanceOf[T]))
	override def clear(): Unit = map.clear()

	override def apply(key: String) = map.get(key)
	override def size = map.size()
	override def isDefinedAt(key: String) = map.containsKey(key)
}