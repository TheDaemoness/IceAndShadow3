package mod.iceandshadow3.util.concurrent

import javax.annotation.Nonnull

/** A thread-safe cache that computes results when not cached.
	* A little bit heavy-handed with its locking.
	*/
class Materializer[K,V](fn: K => V, cacheSize: Int, fair: Boolean = false, freshen: Boolean = false) {
	private val cache = new java.util.LinkedList[(K,V)]
	private val lock = new java.util.concurrent.locks.ReentrantLock(fair)
	def apply(@Nonnull key: K): V = {
		lock.lock()
		try {
			val finder = cache.listIterator()
			var found: (K, V) = null
			while (found == null && finder.hasNext) {
				val current = finder.next()
				if (key.equals(current._1)) found = current
			}
			if (found != null) {
				if(freshen && finder.hasPrevious) {
					finder.remove()
					cache.addFirst(found)
				}
				found._2
			} else {
				while (cache.size() > cacheSize) cache.removeLast()
				val result = fn(key)
				cache.addFirst((key, result))
				result
			}
		} finally {lock.unlock()}
	}
}
