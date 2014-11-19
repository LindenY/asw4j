package ca.uwaterloo.asw;

import java.util.Collection;

public interface DataNodeStore {

	public void add(DataNode dataNode);
	public DataNode get(String name, DataNode.STAGE type);

	public <T extends Collection<DataNode>> T getAllDataNodesWithStage(DataNode.STAGE stage);
}
