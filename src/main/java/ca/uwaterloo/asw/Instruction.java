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
		if (requireData.getClass() == DataNode.class) {
			this.requireData = (R) requireData;
			return;
		}
		this.requireData = (R) requireData.get(requireData.getClass());
	}
	
	public Long getDuration() {
		return duration;
	}
	
	public P getResult() {
		return result;
	}
}
