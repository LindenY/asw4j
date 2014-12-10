package ca.uwaterloo.asw4j;

public interface ToolResolver {
	
	public void registerTool(String name, Object tool);
	public Object getTool(String name);
}
