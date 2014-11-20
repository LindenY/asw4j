package ca.uwaterloo.asw;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface DataNodeStore {

	public void add(DataNode dataNode);
	public void addAll(Collection<DataNode> dataNodes);
	
	
	public DataNode get(String name, DataNode.STAGE stage);
	public DataNode getAndRemove(String name, DataNode.STAGE stage);
	
	public List<DataNode> getAllDataNodesWithStage(DataNode.STAGE stage);
	
	public Iterator<DataNode> iterator(DataNode.STAGE stage);
}
