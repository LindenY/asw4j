package ca.uwaterloo.asw4j;

import java.lang.reflect.InvocationTargetException;
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

public class ThreadPoolWorkerManager extends AbstractWorkerManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(ThreadPoolWorkerManager.class);

	private int coreNumWorkers;
	private int maxNumWorkers;
	
	private volatile Object result;
	private volatile boolean waiting;
	
	private ThreadPool threadPool;
	
	private AtomicLong allJobsTime;
	
	
	public ThreadPoolWorkerManager(
			int coreNumWorkers, 
			int maxNumWorkers,
			DataStore dataStore) {

		this(coreNumWorkers, maxNumWorkers, 30, new SimpleInstructionResolver(dataStore));
	}

	public ThreadPoolWorkerManager(
									int coreNumWorkers, 
									int maxNumWorkers,
									InstructionResolver instructionResolver ) {

		this(coreNumWorkers, maxNumWorkers, 30, instructionResolver);
	}
	
	public ThreadPoolWorkerManager(
									int coreNumWorkers, 
									int maxNumWorkers,
									int maxQueueNum,
									InstructionResolver instructionResolver ) {
		
		super(instructionResolver);

		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(maxQueueNum);
		
		threadPool = new ThreadPool (
				coreNumWorkers, 
				maxNumWorkers, 
				10, TimeUnit.SECONDS, 
				workQueue, 
				this );
		
		waiting = false;
	}

	public <T> Future<T> asyncStart(Class<T> type, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T start(Class<T> type, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public void shutDown() {
	}

	public void shutDownNow() {
	}

	public void awaitShutDown(int timeOut) throws InterruptedException {
	}
	
	private void execute() {
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
					instructionResolver.getDataStore().size()));
			
			startTime = System.currentTimeMillis();
		}
		
		if (threadPool.resolveInstructions() == 0) {
			
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
			instructionResolver.afterInstructionExecution(finishedInstruction, t);
			
			
			if (!addInstructionToExecutionQueue()) {
				if (getActiveCount() <= 1 && getQueue().size() <= 0) {
					synchronized (workerManager) {
						workerManager.notify();
					}
				}
			}
			super.afterExecute(r, t);
		}
		
		public int resolveInstructions() {
			
			Instruction<?, ?> instruction = null;
			int count = 0;
			for (int i=0; i<threadPool.getQueue().remainingCapacity(); i++) {
				try {
					instruction = instructionResolver.resolveInstruction();
				} catch (Exception e){
					shutDownNow();
					return 0;
				}
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
			
			return count;
		}
	}
}
