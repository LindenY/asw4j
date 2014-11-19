package ca.uwaterloo.asw;

public class ThreadPoolWorkerManager<T> extends WorkerManager<T> {

	public ThreadPoolWorkerManager(int coreNumWorkers, int maxNumWorkers,
			ToolResolver toolResolver, Combiner<T> combiner,
			DataNodeStore dataNodeStore, InstructionResolver instructionResolver) {
		super(coreNumWorkers, maxNumWorkers, toolResolver, combiner, dataNodeStore,
				instructionResolver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public T start() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutDownNow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void awaitShutDown(int timeOut) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
}
