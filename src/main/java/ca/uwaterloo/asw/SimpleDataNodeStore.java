package ca.uwaterloo.asw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ca.uwaterloo.asw.DataNode.STAGE;

public class SimpleDataNodeStore implements DataNodeStore<List<DataNode>> {

	private List<DataNode> transitionalDataNodes = new ArrayList<DataNode>();
	private List<DataNode> finalDataNodes = new ArrayList<DataNode>();
	
	public void add(DataNode dataNode) {
		if (dataNode == null) {
			return;
		}
		
		if (dataNode.getStage() == STAGE.TRANSITIONAL) {
			transitionalDataNodes.add(dataNode);
		} else if (dataNode.getStage() == STAGE.FINAL) {
			finalDataNodes.add(dataNode);
		}
	}
	
	public void addAll(Collection<DataNode> dataNodes) {
		Iterator<DataNode> iterator = dataNodes.iterator();
		while(iterator.hasNext()) {
			DataNode next = iterator.next();
			if (next.getStage() == STAGE.TRANSITIONAL) {
				transitionalDataNodes.add(next);
			} else if (next.getStage() == STAGE.FINAL){
				finalDataNodes.add(next);
			}
		}
	}

	public DataNode get(String name, STAGE stage) {
		
		List<DataNode> list = null;
		if (stage == STAGE.TRANSITIONAL) {
			list = transitionalDataNodes;
		} else if (stage == STAGE.FINAL) {
			list = finalDataNodes;
		}
		
		synchronized (this) {
			Iterator<DataNode> iterator = list.iterator();
			while (iterator.hasNext()) {
				DataNode next = iterator.next();
				if (name.equals(next.getName())) {
					list.remove(next);
					return next;
				}
			}
		}
		
		return null;
	}

	public List<DataNode> getAllDataNodesWithStage(STAGE stage) {

		if (stage == STAGE.TRANSITIONAL) {
			return transitionalDataNodes;
		} else if (stage == STAGE.FINAL) {
			return finalDataNodes;
		}
		
		return null;
	}

}
