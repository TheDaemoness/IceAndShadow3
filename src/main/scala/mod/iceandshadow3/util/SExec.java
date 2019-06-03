package mod.iceandshadow3.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Singleton executor.
 */
public class SExec implements ExecutorService {
	public class Future<T> extends FutureTask<T> {
		protected Throwable e = null;
		public Future(Callable<T> arg0) {
			super(arg0);
		}
		public Future(Runnable arg0, T arg1) {
			super(arg0, arg1);
		}
		@Override
		public T get() {
			try {
				return super.get();
			} catch (final Throwable e) {
				this.e = e;
			}
			return null;
		}
		public Throwable getThrown() {
			return e;
		}
	}
	
	private static final SExec instance = new SExec();
	/*public*/ static SExec get() {return instance;}

	public static <T> SExec.Future<T> push(Callable<T> r) {
		final SExec.Future<T> f = instance.new Future<>(r);
		instance.execute(f);
		return f;
	}
	public static <T> SExec.Future<T> push(Runnable r, T arg) {
		final SExec.Future<T> f = instance.new Future<T>(r, arg);
		instance.execute(f);
		return f;
	}
	public static SExec.Future<Object> push(Runnable r) {return push(r, null);}
	
	private ExecutorService executor;
	protected SExec() {
		executor = Executors.newWorkStealingPool(Math.max(1, Runtime.getRuntime().availableProcessors()-1));
	}

	@Override
	public boolean awaitTermination(long arg0, TimeUnit arg1) throws InterruptedException {
		return executor.awaitTermination(arg0, arg1);
	}

	@Override
	public void execute(Runnable arg0) {
		executor.execute(arg0);
	}

	@Override
	public <T> List<java.util.concurrent.Future<T>> invokeAll(
			Collection<? extends Callable<T>> arg0
			) throws InterruptedException {
		return executor.invokeAll(arg0);
	}

	@Override
	public <T> List<java.util.concurrent.Future<T>> invokeAll(
			Collection<? extends Callable<T>> arg0, long arg1, TimeUnit arg2
			) throws InterruptedException {
		return executor.invokeAll(arg0, arg1, arg2);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> arg0)
			throws InterruptedException, ExecutionException {
		return executor.invokeAny(arg0);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> arg0, long arg1, TimeUnit arg2)
			throws InterruptedException, ExecutionException, TimeoutException {
		return executor.invokeAny(arg0, arg1, arg2);
	}

	@Override
	public boolean isShutdown() {
		return executor.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return executor.isTerminated();
	}

	@Override
	public void shutdown() {
		executor.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return executor.shutdownNow();
	}

	@Override
	public <T> java.util.concurrent.Future<T> submit(Callable<T> arg0) {
		return executor.submit(arg0);
	}

	@Override
	public java.util.concurrent.Future<?> submit(Runnable arg0) {
		return executor.submit(arg0);
	}

	@Override
	public <T> java.util.concurrent.Future<T> submit(Runnable arg0, T arg1) {
		return executor.submit(arg0, arg1);
	}
}
