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

		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(maxQueueNum);
		
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
		
		addInstructionToExecutionQueue();

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
			long t = allJobsTime.get();
			LOG.debug(
					String.format(
							"WorkManager finishes with %d jobs complete with duration of %d millis and all jobs time of %d millis.",
							threadPool.getCompletedTaskCount(),
							System.currentTimeMillis() - startTime,
							t));
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
	
	private boolean addInstructionToExecutionQueue() {
		
		Instruction<?, ?> instruction = null;
		int count = 0;
		for (int i=0; i<threadPool.getQueue().remainingCapacity(); i++) {
			instruction = instructionResolver.resolveInstruction();
			if (instruction == null) {
				break;
			}
			
			threadPool.execute(instruction);
			count ++;
		}
		
		if (LOG.isTraceEnabled()) {
			LOG.trace(
				String.format(
					"%d active workers, %d instructions in queue, %d capacity remain, %d instructions added.",
						threadPool.getActiveCount(),
						threadPool.getQueue().size(),
						threadPool.getQueue().remainingCapacity(),
						count));
		}
		
		
		if (count == 0) {
			return false;
		}
		return true;
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
			
			if (LOG.isTraceEnabled()) {
				LOG.trace(
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
			
			if (LOG.isTraceEnabled()) {
				LOG.trace(
					String.format("%s finished run with duration %d.", 
							finishedInstruction.getClass().getName(),
							finishedInstruction.getDuration()));
			}

			if (finishedInstruction.getDuration() != null && finishedInstruction.getDuration() >= 0) {
				workerManager.allJobsTime.addAndGet(finishedInstruction.getDuration());
			}
			workerManager.addInstructionProduceDataToDataStore(finishedInstruction);
			instructionResolver.afterInstructionExecution(finishedInstruction);
			
			
			if (!addInstructionToExecutionQueue()) {
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
