package mod.iceandshadow3.lib.util;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

/** @deprecated DO NOT USE WITHOUT TESTING!
 * Chainable system for providing a same-type output as an input object.
 * 
 * To customize functionality, overload getNext, getSelf, getResult, and
 * getMessage. Ideally, these are stateless, allowing reuse across multiple
 * callers.
 */
public abstract class BPipeline<T> implements Iterator<T>, Function<T, T> {
	private BPipeline<T> next;
	private Supplier<T> callback;

	public BPipeline() {
	}

	public BPipeline(BPipeline<T> tocopy) {
		this.callback = tocopy.callback;
		this.next = tocopy.next;
	}

	@Override
	public final T apply(T in) {
		Object msg = null, prevmsg = null;

		BPipeline<T> current = getSelf(in, msg);

		while (current != null) {
			final Object selfmsg = current.getSelfMessage(in, prevmsg);
			in = current.getResult(in, msg, selfmsg);
			msg = current.getNextMessage(in, msg, selfmsg);
			current = current.getNext(in, prevmsg, selfmsg);
			prevmsg = msg;
			if (current != null)
				current = current.getSelf(in, msg);
		}
		return in;
	}

	protected BPipeline<T> getNext(T in, Object prevmsg, Object selfmsg) {
		return next;
	}

	protected Object getNextMessage(T in, Object prevmsg, Object selfmsg) {
		return null;
	}

	protected abstract T getResult(T in, Object prevmsg, Object selfmsg);

	protected BPipeline<T> getSelf(T in, Object prevmsg) {
		return in != null ? this : null;
	}

	protected Object getSelfMessage(T in, Object prevmsg) {
		return null;
	}

	@Override
	public boolean hasNext() {
		return callback != null;
	}

	@Override
	public final T next() {
		try {
			return apply(callback.get());
		} catch (final Exception e) {
			return null;
		}
	}

	public BPipeline<T> setCallback(Supplier<T> supplier) {
		callback = supplier;
		return this;
	}

	public BPipeline<T> setInput(final T input) {
		callback = () -> input;
		return this;
	}

	public BPipeline<T> setNext(BPipeline<T> mut) {
		next = mut;
		return mut == null ? this : mut;
	}
}
