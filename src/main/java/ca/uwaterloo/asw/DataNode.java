package ca.uwaterloo.asw;

public class DataNode {
	
	public static enum STAGE {
		TRANSITIONAL,
		FINAL
	}

	private String name;
	private STAGE type;
	private Object data;
	
	public DataNode(String name, STAGE type, Object data) {
		super();
		this.name = name;
		this.type = type;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public STAGE getStage() {
		return type;
	}

	public Object getData() {
		return data;
	}
	
	
}
