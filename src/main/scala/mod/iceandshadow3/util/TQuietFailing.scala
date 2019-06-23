package mod.iceandshadow3.util

/** Trait for classes that can fail in a programmer-ignorable way.
	*/
trait TQuietFailing {
	private var _failure: Exception = _
	def failure = Option(_failure)
	def failed = {_failure != null}
	protected def setFailure(e: Exception): Unit = {_failure = e}
	protected def setFailure(condition: Boolean, e: => Exception): Unit =
		if(condition) setFailure(e) else clearFailure()
	def clearFailure(): Unit = {_failure = null}
}
