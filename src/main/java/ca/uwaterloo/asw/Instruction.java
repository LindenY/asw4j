package ca.uwaterloo.asw;

public abstract  class Instruction implements Runnable {
	
	public abstract void setRawDataNodes(DataNode[] rawDataNodes);
	public abstract void setToolbox(ToolResolver toolResolver);
	
	public abstract void preExecution();
	public abstract void execute();
	public abstract void postExecution();

	public final void run() {
		execute();
	}
}
