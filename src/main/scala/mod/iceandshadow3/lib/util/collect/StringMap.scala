package mod.iceandshadow3.lib.util.collect

import scala.collection.mutable
import scala.jdk.CollectionConverters._

class StringMap[T] extends mutable.Map[String, T] {
	//TODO: Quick-n-dirty implementation that will need polishing later, likely conversion to a trie.
	private val map = new java.util.HashMap[String, T]
	var success = true

	def isValidKey(key: String): Boolean = true
	def iterator = map.asScala.iterator

	/** Inserts a key-value pair if the key is valid. */
	override def addOne(kv: (String, T)) = {
		success = isValidKey(kv._1)
		if(success) map.put(kv._1, kv._2)
		this
	}
	override def subtractOne(key: String) = {
		success = map.remove(key) != null; this
	}
	override def get(key: String) = Option(map.get(key))
	override def clear(): Unit = {
		success = true
		map.clear()
	}

	override def apply(key: String) = map.get(key)
	override def size = map.size()
	override def isDefinedAt(key: String) = map.containsKey(key)
}