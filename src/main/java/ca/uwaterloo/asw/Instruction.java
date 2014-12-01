package ca.uwaterloo.asw;

import java.util.Iterator;

import ca.uwaterloo.asw.DataNode;
import ca.uwaterloo.asw.reflection.TypeToken;

public abstract  class Instruction<R, P> implements Runnable {
	
	public Instruction(ToolResolver toolResolver) {
		
	}
	
	public abstract void preExecution();
	public abstract P execute(R requiredData);
	public abstract void postExecution();

	public final void run() {
		
		Long st = System.currentTimeMillis();
		
		preExecution();
		setResult(execute(requireData));
		postExecution();
		
		setDuration(System.currentTimeMillis() - st);
	}
	
	protected Long duration;
	protected P result;
	protected R requireData;
	
	private void setDuration(Long duration) {
		this.duration = duration;
	}
	
	private void setResult(P result) {
		this.result = result;
	}
	
	@SuppressWarnings("unchecked")
	public void setRequireData(DataNode requireData) {
		
		
	}
	
	public Long getDuration() {
		return duration;
	}
	
	public P getResult() {
		return result;
	}
}
