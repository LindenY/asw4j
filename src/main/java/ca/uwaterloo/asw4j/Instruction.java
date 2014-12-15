package ca.uwaterloo.asw4j;



public abstract class Instruction<R, P> implements Runnable {
	
	public Instruction() {
	}
	
	public Instruction(ToolResolver toolResolver) {
	}
	
	public abstract P execute(R requireData);

	public final void run() {
		
		Long st = System.currentTimeMillis();
		
		preExecution();
		setResult(execute(requireData));
		postExecution();
		
		setDuration(System.currentTimeMillis() - st);
	}
	
	public void preExecution() {
	}

	public void postExecution() {
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
