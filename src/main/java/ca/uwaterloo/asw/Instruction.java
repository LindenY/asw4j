package ca.uwaterloo.asw;

import java.util.Map;

public abstract  class Instruction implements Runnable {
	
	private ToolResolver toolResolver;
	
	public abstract void setRawDataNodes(DataNode[] rawDataNodes);
	public abstract void setToolbox();
	
	public abstract void preExecution();
	public abstract void execute();
	public abstract void postExecution();

	public final void run() {
		execute();
	}
}
