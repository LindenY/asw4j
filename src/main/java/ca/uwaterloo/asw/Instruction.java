package ca.uwaterloo.asw;

import ca.uwaterloo.asw.DataNode;

public abstract  class Instruction<R, P> implements Runnable {
	
	public Instruction(ToolResolver toolResolver) {
		
	}
	
	public abstract void preExecution();
	public abstract P execute(R requiredData);
	public abstract void postExecution();

	public final void run() {
		
		Long st = System.currentTimeMillis();
		
		preExecution();
		setResult(execute(requiredData));
		postExecution();
		
		setDuration(System.currentTimeMillis() - st);
	}
	
	protected Long duration;
	protected P result;
	protected R requiredData;
	
	private void setDuration(Long duration) {
		this.duration = duration;
	}
	
	private void setResult(P result) {
		this.result = result;
	}
	
	@SuppressWarnings("unchecked")
	public void setRequiredData(DataNode requiredData) {
		if (requiredData.getClass() == DataNode.class) {
			this.requiredData = (R) requiredData;
			return;
		}
		this.requiredData = (R) requiredData.get(requiredData.getClass());
	}
	
	public Long getDuration() {
		return duration;
	}
	
	public P getResult() {
		return result;
	}
}
