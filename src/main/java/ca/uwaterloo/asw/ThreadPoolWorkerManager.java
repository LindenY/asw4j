package ca.uwaterloo.asw;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPoolWorkerManager<T> extends WorkerManager<T> {

	private static final Logger LOG = LoggerFactory
			.getLogger(ThreadPoolWorkerManager.class);

	private ThreadPool threadPool;
	
	private AtomicLong allJobsTime;

	public ThreadPoolWorkerManager(
									int coreNumWorkers, 
									int maxNumWorkers,
									DataStore dataStore,
									InstructionResolver instructionResolver ) {

		this(coreNumWorkers, maxNumWorkers, 30, dataStore, instructionResolver);
	}
	
	public ThreadPoolWorkerManager(
									int coreNumWorkers, 
									int maxNumWorkers,
									int maxQueueNum,
									DataStore dataStore,
									InstructionResolver instructionResolver ) {
		
		super(coreNumWorkers, maxNumWorkers, dataStore, instructionResolver);

		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(maxNumWorkers);
		
		threadPool = new ThreadPool(
				coreNumWorkers, 
				maxNumWorkers, 
				10, TimeUnit.SECONDS, 
				workQueue, 
				this );
		
	}

	@Override
	public T start() {
		
		allJobsTime = new AtomicLong(0);
		
		Long startTime = null;
		if (LOG.isDebugEnabled()) {
			LOG.debug(
				String.format(
					"WorkerManager starts working with " + 
					"%d core workers, %d max workers, " + 
					"%d registered instruction classses " +
					"and %d data objects.",
					coreNumWorkers,
					maxNumWorkers,
					instructionResolver.numberOfRegisteredInstruction(),
					dataStore.size()));
			
			startTime = System.currentTimeMillis();
		}

		Instruction<?, ?> instruction = instructionResolver
				.resolveInstruction();
		while (instruction != null) {
			threadPool.execute(instruction);
			instruction = instructionResolver.resolveInstruction();
		}

		T executedResult = null;
		if (threadPool.getActiveCount() > 0) {
			try {
				LOG.debug("Start waiting");
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				LOG.debug("Wake up from " + e.getLocalizedMessage());
			}

			threadPool.shutdown();
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug(
					String.format(
							"WorkManager finishes with %d jobs complete " + 
							"with duration of %d millis " +
							"and all jobs time of %d millis.",
							threadPool.getCompletedTaskCount(),
							System.currentTimeMillis() - startTime,
							allJobsTime));
		}

		return executedResult;
	}

	@Override
	public void shutDown() {
	}

	@Override
	public void shutDownNow() {
	}

	@Override
	public void awaitShutDown(int timeOut) throws InterruptedException {
	}

	private class ThreadPool extends ThreadPoolExecutor {

		private ThreadPoolWorkerManager<T> workerManager;

		public ThreadPool(int corePoolSize, int maximumPoolSize,
				long keepAliveTime, TimeUnit unit,
				BlockingQueue<Runnable> workQueue,
				ThreadPoolWorkerManager<T> workerManager) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
			this.workerManager = workerManager;
		}

		@Override
		protected void beforeExecute(Thread t, Runnable r) {
			
			if (LOG.isDebugEnabled()) {
				LOG.debug(
					String.format(
						"%s starts to run on thread %s",
						r.getClass().getName(),
						t.getName()));
			}
			
			instructionResolver
					.beforInstructionExecution((Instruction<?, ?>) r);
			super.beforeExecute(t, r);
		}

		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			
			Instruction<?, ?> finishedInstruction = (Instruction<?, ?>) r;
			
			if (LOG.isDebugEnabled()) {
				LOG.debug(
					String.format("%s finished run with duration %d.", 
							finishedInstruction.getClass().getName(),
							finishedInstruction.getDuration()));
			}

			workerManager.allJobsTime.addAndGet(finishedInstruction.getDuration());
			workerManager.addInstructionProduceDataToDataStore(finishedInstruction);
			instructionResolver.afterInstructionExecution(finishedInstruction);
			
			Instruction<?, ?> newInstruction = instructionResolver
					.resolveInstruction();
			
			int resolvedCount = 0;
			while (newInstruction != null) {
				resolvedCount ++;
				execute(newInstruction);
				newInstruction = instructionResolver.resolveInstruction();
			}
			
			if (resolvedCount == 0) {
				if (getActiveCount() <= 1 && getQueue().size() <= 0) {
					synchronized (workerManager) {
						workerManager.notify();
					}
				}
			}
			super.afterExecute(r, t);
		}
	}

}
