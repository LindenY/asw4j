package ca.uwaterloo.asw;

import java.util.Date;

public abstract  class Instruction implements Runnable {
	
	public abstract void setRawDataNodes(DataNode[] rawDataNodes);
	public abstract void setToolbox(ToolResolver toolResolver);
	
	public abstract void preExecution();
	public abstract void execute();
	public abstract void postExecution();
	
	public abstract DataNode getResult();

	public final void run() {
		preExecution();
		Date d = new Date();
		execute();
		executionDuration = new Date().getTime() - d.getTime();
		postExecution();
	}
	
	private long executionDuration;
	public long getExecutionDuration() {
		return executionDuration;
	}
}
