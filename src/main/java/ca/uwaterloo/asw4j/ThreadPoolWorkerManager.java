package ca.uwaterloo.asw4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uwaterloo.asw4j.reflection.TypeToken;

/**
 * <p>
 * An implementation of {@link WorkerManager} which uses pooling technique to
 * recycle the "workers"({@link Thread}s). By recycling and reusing the
 * {@link Thread}s, {@link ThreadPoolWorkerManager} reduces the system resources
 * usage and boost the performance.
 * </p>
 * <p>
 * {@link ThreadPoolWorkerManager} integrates with <a
 * href="http://www.slf4j.org/">SLF4J logging API</a> to provide insightful
 * information during execution. It categories execution messages into three
 * different logging level ( {@link Logger#trace(String)},
 * {@link Logger#debug(String)}, and {@link Logger#info(String)}) to meet the
 * diversity needs of different stages of development. To know more about how to
 * integrate SLF4J, visit <a
 * href="http://www.slf4j.org/">http://www.slf4j.org/</a>
 * </p>
 * 
 * @see <a herf="http://www.slf4j.org/">http://www.slf4j.org/</a>
 * @author Desmond Lin
 * @since 1.0.0
 */
public class ThreadPoolWorkerManager extends AbstractWorkerManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(ThreadPoolWorkerManager.class);

	private int coreNumWorkers;
	private int maxNumWorkers;

	private ThreadPool threadPool;

	private AtomicLong allJobsTime;
	private boolean waiting;
	private long startTime;

	public ThreadPoolWorkerManager(int coreNumWorkers, int maxNumWorkers,
			DataStore dataStore) {

		this(coreNumWorkers, maxNumWorkers, 30, new SimpleInstructionResolver(
				dataStore));
	}

	public ThreadPoolWorkerManager(int coreNumWorkers, int maxNumWorkers,
			InstructionResolver instructionResolver) {

		this(coreNumWorkers, maxNumWorkers, 30, instructionResolver);
	}

	public ThreadPoolWorkerManager(int coreNumWorkers, int maxNumWorkers,
			int maxQueueNum, InstructionResolver instructionResolver) {

		super(instructionResolver);

		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
				maxQueueNum);

		threadPool = new ThreadPool(coreNumWorkers, maxNumWorkers, 10,
				TimeUnit.SECONDS, workQueue, this);

		waiting = false;
	}

	public <T> Future<T> asyncStart(Class<T> type, String name) {
		startExecution();
		return new FutureTask<T>(this, TypeToken.get(type, name)) {
		};
	}

	public <T> T start(Class<T> type, String name) {
		T result = null;
		try {
			result = asyncStart(type, name).get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
			if (LOG.isDebugEnabled()) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private void startExecution() {

		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("WorkerManager starts working with "
					+ "%d core workers, %d max workers, "
					+ "%d registered instruction classses "
					+ "and %d data objects.", coreNumWorkers, maxNumWorkers,
					instructionResolver.numberOfRegisteredInstruction(),
					instructionResolver.getDataStore().size()));

			startTime = System.currentTimeMillis();
			allJobsTime = new AtomicLong(0);
		}

		state = STATE.Running;

		if (threadPool.resolveInstructions() == 0) {
			finishExecution();
		}
	}

	private void cancelExecution(boolean mayInterruptIfRunning) {

		if (mayInterruptIfRunning) {
			threadPool.shutdownNow();
		} else {
			threadPool.shutdown();
		}

		if (waiting) {
			synchronized (this) {
				this.notify();
			}
		}
		state = STATE.Canceled;
	}

	private void finishExecution() {
		if (waiting) {
			synchronized (this) {
				this.notify();
			}
		}

		if (!threadPool.isShutdown()) {
			state = STATE.Finished;
		}

		if (LOG.isDebugEnabled()) {
			long t = allJobsTime.get();
			LOG.debug(String
					.format("WorkManager finishes with %d jobs complete with duration of %d millis and all jobs time of %d millis.",
							threadPool.getCompletedTaskCount(),
							System.currentTimeMillis() - startTime, t));
		}
	}

	private class ThreadPool extends ThreadPoolExecutor {

		private ThreadPoolWorkerManager workerManager;

		public ThreadPool(int corePoolSize, int maximumPoolSize,
				long keepAliveTime, TimeUnit unit,
				BlockingQueue<Runnable> workQueue,
				ThreadPoolWorkerManager workerManager) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
			this.workerManager = workerManager;
		}

		@Override
		protected void beforeExecute(Thread t, Runnable r) {

			if (LOG.isTraceEnabled()) {
				LOG.trace(String.format("%s starts to run on thread %s", r
						.getClass().getName(), t.getName()));
			}

			instructionResolver
					.beforeInstructionExecution((Instruction<?, ?>) r);
			super.beforeExecute(t, r);
		}

		@Override
		protected void afterExecute(Runnable r, Throwable t) {

			Instruction<?, ?> finishedInstruction = (Instruction<?, ?>) r;

			if (LOG.isTraceEnabled()) {
				LOG.trace(String.format("%s finished run with duration %d.",
						finishedInstruction.getClass().getName(),
						finishedInstruction.getDuration()));
			}

			if (LOG.isDebugEnabled()) {
				if (finishedInstruction.getDuration() != null
						&& finishedInstruction.getDuration() >= 0) {
					workerManager.allJobsTime.addAndGet(finishedInstruction
							.getDuration());
				}
			}

			instructionResolver.afterInstructionExecution(finishedInstruction,
					t);

			if (resolveInstructions() == 0 || isShutdown()) {
				if (getActiveCount() <= 1 && getQueue().size() <= 0) {
					workerManager.finishExecution();
				}
			}
			super.afterExecute(r, t);
		}

		public int resolveInstructions() {

			Instruction<?, ?> instruction = null;
			int count = 0;
			for (int i = 0; i < threadPool.getQueue().remainingCapacity(); i++) {
				try {
					instruction = instructionResolver.resolveInstruction();
				} catch (Exception e) {
					shutdownNow();
					state = STATE.TerminatedByException;
					return 0;
				}
				if (instruction == null) {
					break;
				}

				threadPool.execute(instruction);
				count++;
			}

			if (LOG.isTraceEnabled()) {
				LOG.trace(String
						.format("%d active workers, %d instructions in queue, %d capacity remain, %d instructions added.",
								threadPool.getActiveCount(), threadPool
										.getQueue().size(), threadPool
										.getQueue().remainingCapacity(), count));
			}

			return count;
		}
	}

	private abstract class FutureTask<T> implements Future<T> {

		private WorkerManager workerManager;
		private TypeToken<?> resultTypeToken;

		public FutureTask(WorkerManager workerManager,
				TypeToken<?> resultTypeToken) {
			this.workerManager = workerManager;
			this.resultTypeToken = resultTypeToken;
		}

		public boolean cancel(boolean mayInterruptIfRunning) {
			cancelExecution(mayInterruptIfRunning);
			return true;
		}

		public boolean isCancelled() {
			if (getState() == STATE.Canceled) {
				return true;
			}
			return false;
		}

		public boolean isDone() {
			if (getState() != STATE.Ready && getState() != STATE.Running) {
				return true;
			}
			return false;
		}

		@SuppressWarnings({ "unchecked", "finally" })
		public T get() throws InterruptedException, ExecutionException {
			T result = null;
			if (state == STATE.Running) {
				try {
					synchronized (workerManager) {
						waiting = true;
						workerManager.wait();
					}
				} catch (InterruptedException e) {
					if (state == STATE.Finished) {
						result = (T) instructionResolver.getDataStore()
								.combineAndGet(resultTypeToken);
						return result;
					}
				} finally {
					throw new InterruptedException();
				}
			}

			return result;
		}

		public T get(long timeout, TimeUnit unit) throws InterruptedException,
				ExecutionException, TimeoutException {
			threadPool.awaitTermination(timeout, unit);
			return get();
		}

	}
}
