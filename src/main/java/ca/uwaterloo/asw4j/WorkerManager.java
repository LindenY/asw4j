package ca.uwaterloo.asw4j;

import java.util.concurrent.Future;

public interface WorkerManager<T> {
	
	public Future<T> asyncStart();
	
	public void awaitShutDown(int timeOut) throws InterruptedException;
	
	public void shutDown();
	
	public void shutDownNow();
	
	public T start();

}
