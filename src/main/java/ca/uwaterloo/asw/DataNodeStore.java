package ca.uwaterloo.asw;

import java.util.Collection;

public interface DataNodeStore <C extends Collection<DataNode>> {

	public void add(DataNode dataNode);
	public void addAll(Collection<DataNode> dataNodes);
	public DataNode get(String name, DataNode.STAGE type);

	public C getAllDataNodesWithStage(DataNode.STAGE stage);
}
