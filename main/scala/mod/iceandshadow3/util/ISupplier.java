package mod.iceandshadow3.util;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Interface for a general use supplying-type class that can fill most "provide
 * objects of type T" roles.
 * 
 * Includes quick-and-dirty implementations of most of its functions.
 */
public interface ISupplier<T> extends Iterator<T>, Iterable<T>, Callable<T>, Supplier<T> {
	@Override
	public default T call() throws Exception {
		if (hasNext()) return next();
		return null;
	}
	
	@Override
	public default T get() {
		try {return call();}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public default Iterator<T> iterator() {
		return this;
	}
}
