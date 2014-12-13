package ca.uwaterloo.asw;


import ca.uwaterloo.asw.DataNode;

public abstract  class Instruction<R, P> implements Runnable {
	
	public Instruction(ToolResolver toolResolver) {
		
	}
	
	public abstract void preExecution();
	public abstract P execute(R requireData);
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
		if (requireData.size() == 1) {
			this.requireData = (R) requireData.values().get(0);
			return;
		}
		this.requireData = (R) requireData;
	}
	
	public Long getDuration() {
		return duration;
	}
	
	public P getResult() {
		return result;
	}
}
