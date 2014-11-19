package ca.uwaterloo.asw;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uwaterloo.asw.DataNode.STAGE;

public class ThreadPoolWorkerManager<T> extends WorkerManager<T> {

	private static final Logger LOG = LoggerFactory
			.getLogger(ThreadPoolWorkerManager.class);

	private ThreadPool threadPool;

	public ThreadPoolWorkerManager(int coreNumWorkers, int maxNumWorkers,
			ToolResolver toolResolver, Combiner<T> combiner,
			DataNodeStore dataNodeStore, InstructionResolver instructionResolver) {

		super(coreNumWorkers, maxNumWorkers, toolResolver, combiner,
				dataNodeStore, instructionResolver);

		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(30);
		threadPool = new ThreadPool(coreNumWorkers, coreNumWorkers, 10,
				TimeUnit.SECONDS, workQueue);

		LOG.debug(String
				.format("ThreadPoolWorkerManager instantialized with [coreNumWorkers:%d], [maxNumWorkers:%d]",
						coreNumWorkers, maxNumWorkers));
	}

	@Override
	public T start() {
		LOG.debug(String
				.format("Invoked Start() with [DataNodeStore.size:%d], [InstructionResolver.size:%d]",
						dataNodeStore
								.getAllDataNodesWithStage(STAGE.TRANSITIONAL).size(),
						instructionResolver.numberOfRegisteredInstruction()));

		synchronized (this) {
			while (dataNodeStore.getAllDataNodesWithStage(STAGE.TRANSITIONAL)
					.size() > 0 || threadPool.getActiveCount() > 0) {

				Instruction instruction = instructionResolver
						.resolveInstruction();
				while (instruction != null) {
					threadPool.execute(instruction);
					instruction = instructionResolver.resolveInstruction();
				}

				/*
				try {
					LOG.debug("Start waiting");
					this.wait();
				} catch (InterruptedException e) {
					LOG.debug("Wake up from " + e.getLocalizedMessage());
				}
				*/
			}
		}

		threadPool.shutdown();

		Collection<DataNode> dataNodes = dataNodeStore
				.getAllDataNodesWithStage(STAGE.FINAL);
		return combiner.combineDataNodes(dataNodes);
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

		private Map<Runnable, Date> timeMap = new HashMap<Runnable, Date>();

		public ThreadPool(int corePoolSize, int maximumPoolSize,
				long keepAliveTime, TimeUnit unit,
				BlockingQueue<Runnable> workQueue) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		@Override
		protected void beforeExecute(Thread t, Runnable r) {
			LOG.debug(String.format(
					"Before Execute [Instruction:%s] on [Thread:%s]", r
							.getClass().getName(), t.getName()));

			timeMap.put(r, new Date());

			instructionResolver.beforInstructionExecution((Instruction) r);
			super.beforeExecute(t, r);
		}

		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			long duration = (new Date()).getTime() - timeMap.get(r).getTime();
			LOG.debug(String.format(
					"After execute [Instruction:%s] with duration %d", r
							.getClass().getName(), duration));

			Instruction instruction = (Instruction) r;
			dataNodeStore.add(instruction.getResult());
			instructionResolver.afterInstructionExecution(instruction);
			super.afterExecute(r, t);
		}
	}
}
