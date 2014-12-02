package ca.uwaterloo.asw;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPoolWorkerManager<T> extends WorkerManager<T> {

	private static final Logger LOG = LoggerFactory
			.getLogger(ThreadPoolWorkerManager.class);

	private ThreadPool threadPool;

	public ThreadPoolWorkerManager(int coreNumWorkers, int maxNumWorkers,
			ToolResolver toolResolver, DataStore dataNodeStore,
			InstructionResolver instructionResolver) {

		super(coreNumWorkers, maxNumWorkers, toolResolver, dataNodeStore,
				instructionResolver);

		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(30);
		threadPool = new ThreadPool(coreNumWorkers, coreNumWorkers, 10,
				TimeUnit.SECONDS, workQueue, this);

		LOG.debug(String
				.format("ThreadPoolWorkerManager instantialized with [coreNumWorkers:%d], [maxNumWorkers:%d]",
						coreNumWorkers, maxNumWorkers));
	}

	@Override
	public T start() {

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
			instructionResolver
					.beforInstructionExecution((Instruction<?, ?>) r);
			super.beforeExecute(t, r);
		}

		@Override
		protected void afterExecute(Runnable r, Throwable t) {

			Instruction<?, ?> finishedInstruction = (Instruction<?, ?>) r;
			workerManager.addInstructionProduceDataToDataStore(finishedInstruction);
			instructionResolver.afterInstructionExecution(finishedInstruction);
			
			Instruction<?, ?> newInstruction = instructionResolver
					.resolveInstruction();
			if (newInstruction == null) {
				if (getActiveCount() <= 1 && getQueue().size() <= 0) {
					synchronized (workerManager) {
						workerManager.notify();
					}
				}
			} else {
				execute(newInstruction);
			}
			super.afterExecute(r, t);
		}
	}

}
