package mod.iceandshadow3.util;

import java.util.Iterator;
import java.util.concurrent.Callable;

/**
 * Interface for a general use supplying-type class that can fill most "provide
 * objects of type T" roles.
 * 
 * Includes quick-and-dirty implementations of call() and iterator(), previously found in BaseSupplier.
 */
public interface ISupplier<T> extends Iterator<T>, Iterable<T>, Callable<T> {
	@Override
	public default T call() throws Exception {
		if (hasNext())
			return next();
		return null;
	}

	@Override
	public default Iterator<T> iterator() {
		return this;
	}
}
