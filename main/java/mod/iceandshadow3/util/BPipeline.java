package mod.iceandshadow3.util;

import java.util.concurrent.Callable;

/**
 * Chainable system for providing a same-type output as an input object.
 * 
 * To customize functionality, overload getNext, getSelf, getResult, and
 * getMessage. Ideally, these are stateless, allowing reuse across multiple
 * callers.
 */
public abstract class BPipeline<T> implements ISupplier<T> {
	private BPipeline<T> next;
	private Callable<T> callback;

	public BPipeline() {
	}

	public BPipeline(BPipeline<T> tocopy) {
		this.callback = tocopy.callback;
		this.next = tocopy.next;
	}

	@Override
	public final T call() throws Exception {
		return call(callback.call());
	}

	public final T call(T in) {
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
			return call();
		} catch (final Exception e) {
			return null;
		}
	}

	public BPipeline<T> setCallback(Callable<T> supplier) {
		callback = supplier;
		return this;
	}

	public BPipeline<T> setInput(final T input) {
		callback = new Callable<T>() {
			@Override
			public T call() {
				return input;
			}
		};
		return this;
	}

	public BPipeline<T> setNext(BPipeline<T> mut) {
		next = mut;
		return mut == null ? this : mut;
	}
}
