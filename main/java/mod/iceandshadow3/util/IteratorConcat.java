package mod.iceandshadow3.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements a list of iterators whose outputs are seamlessly concatenated.
 */
public class IteratorConcat<T, It extends Iterator<T>> implements ISupplier<T>, IResetable {
	protected List<It> iters;
	protected Iterator<It> listiter;
	protected It currentiter;

	public IteratorConcat() {
		iters = new LinkedList<It>();
	}

	public boolean add(It newiter) {
		iters.add(newiter);
		reset();
		return true;
	}

	protected void assureValidIter() {
		while (!currentiter.hasNext())
			if (listiter.hasNext())
				currentiter = listiter.next();
			else {
				currentiter = null;
				break;
			}
	}

	@Override
	public boolean hasNext() {
		assureValidIter();
		return currentiter != null;
	}

	@Override
	public T next() {
		assureValidIter();
		return currentiter != null ? currentiter.next() : null;
	}

	/**
	 * Resets the collection as much as possible. Does NOT reset contained
	 * iterators.
	 */
	@Override
	public boolean reset() {
		listiter = iters.iterator();
		currentiter = listiter.next();
		return false; // Does not reset individual iterators.
	}
}
