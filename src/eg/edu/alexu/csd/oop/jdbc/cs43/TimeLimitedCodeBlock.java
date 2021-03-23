package eg.edu.alexu.csd.oop.jdbc.cs43;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class TimeLimitedCodeBlock {
	// runnable run has no return type vs callable return object
	public static void runWithTimeout(Runnable runnable, long timeout, TimeUnit timeUnit) {
		runWithTimeout(new Callable<Object>() {
			@Override
			public Object call() {
				runnable.run();
				return null;
			}
		}, timeout, timeUnit);
	}

	public static <T> void runWithTimeout(Callable<T> callable, long timeout, TimeUnit timeUnit) {

		final ExecutorService executor = Executors.newSingleThreadExecutor();
		final Future<T> future = executor.submit(callable);
		executor.shutdown(); // closes the executor after the callable finishes
		try {

			future.get(timeout, timeUnit);

		} catch (TimeoutException e) {
			// the runnable is cancelled after time out
			future.cancel(true);
			MyLogger.getLogger().log(Level.WARNING, "query time out");
		} catch (ExecutionException e) {
			Throwable t = e.getCause();
			if (t instanceof SQLException) {
				MyLogger.getLogger().log(Level.WARNING, "sql exception", e);
			}
		} catch (InterruptedException e) {

		}
	}
}