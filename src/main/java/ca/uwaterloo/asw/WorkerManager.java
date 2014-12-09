package ca.uwaterloo.asw;

import java.util.concurrent.Future;

public interface WorkerManager<T> {
	
	public T start();
	
	public Future<T> asyncStart();
	
	public void shutDown();
	
	public void shutDownNow();
	
	public void awaitShutDown(int timeOut) throws InterruptedException;

}
