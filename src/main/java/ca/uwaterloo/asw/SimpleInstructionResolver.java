package ca.uwaterloo.asw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uwaterloo.asw.DataNode.STAGE;

public class LinkedListStore implements DataNodeStore {

	/*
	 * private ThreadLocal<Iterator<DataNode>> threadLocalIterator = new
	 * ThreadLocal<Iterator<DataNode>>() {
	 * 
	 * @Override protected Iterator<DataNode> initialValue() { return new
	 * LocalIterator(linkedList); } };
	 */

	private static final Logger LOG = LoggerFactory
			.getLogger(LinkedListStore.class);

	private LinkedList linkedList = new LinkedList();
	private List<DataNode> finalDataNodes = new ArrayList<DataNode>();

	public void add(DataNode dataNode) {
		if (dataNode.getStage() == STAGE.TRANSITIONAL) {
			linkedList.add(dataNode);
		} else if (dataNode.getStage() == STAGE.FINAL) {
			finalDataNodes.add(dataNode);
		}
	}

	public void addAll(Collection<DataNode> dataNodes) {
		Iterator<DataNode> itr = dataNodes.iterator();
		while (itr.hasNext()) {
			add(itr.next());
		}
	}

	public DataNode get(String name, STAGE stage) {
		Iterator<DataNode> itr = null;
		itr = iterator(stage);
		while (itr.hasNext()) {
			DataNode next = itr.next();
			if (next.getName().equals(name)) {
				return next;
			}
		}
		return null;
	}

	public DataNode getAndRemove(String name, STAGE stage) {
		DataNode dn = get(name, stage);

		if (dn == null) {
			return null;
		}

		if (dn.getStage() == STAGE.TRANSITIONAL) {
			linkedList.remove(dn);
		} else if (dn.getStage() == STAGE.FINAL) {
			synchronized (finalDataNodes) {
				finalDataNodes.remove(dn);
			}
		}
		return dn;
	}

	public List<DataNode> getAllDataNodesWithStage(STAGE stage) {
		if (stage == STAGE.TRANSITIONAL) {
			List<DataNode> tdn = new ArrayList<DataNode>();

			Iterator<DataNode> itr = iterator(stage);
			
			int count = 0;
			while (itr.hasNext()) {
				tdn.add(itr.next());
			}
			return tdn;
		} else if (stage == STAGE.FINAL) {
			return finalDataNodes;
		}

		return null;
	}

	public Iterator<DataNode> iterator(STAGE stage) {
		if (stage == STAGE.TRANSITIONAL) {
			return new LocalIterator(linkedList);
		} else if (stage == STAGE.FINAL) {
			return finalDataNodes.iterator();
		}
		return null;
	}

	static class LocalIterator implements Iterator<DataNode> {

		LinkedList linkedList;
		LinkedNode currentNode;

		public LocalIterator(LinkedList linkedList) {
			this.linkedList = linkedList;
			this.currentNode = linkedList.getHeader();
		}

		public boolean hasNext() {
			if (currentNode == null) {
				return false;
			}
			return true;
		}

		public DataNode next() {
			DataNode temp = currentNode.getData();
			currentNode = currentNode.getNext();
			
			return temp;
		}

		public void remove() {
			linkedList.remove(currentNode);
		}
	}

	static class LinkedList {

		private LinkedNode header;
		private LinkedNode tail;

		public LinkedNode getHeader() {
			return header;
		}

		public LinkedNode getTail() {
			return tail;
		}

		public void add(DataNode dataNode) {
			if (dataNode == null) {
				return;
			}
			
			if (tail == null) {
				synchronized (this) {
					tail = new LinkedNode(dataNode, null, null);
					header = tail;
				}
			} else {
				synchronized (tail) {
					LinkedNode previous = tail.getPrevious() == null ? header
							: tail;
					tail = new LinkedNode(dataNode, null, previous);
					previous.setNext(tail);
				}
			}
		}

		public void remove(DataNode dataNode) {
			LinkedNode currentLinkedNode = header;
			while (currentLinkedNode != null) {
				if (currentLinkedNode.getData() == dataNode) {
					remove(currentLinkedNode);
					return;
				}
				currentLinkedNode = currentLinkedNode.getNext();
			}
		}

		public void remove(LinkedNode linkedNode) {
			if (linkedNode == null) {
				return;
			}
			
			synchronized (linkedNode) {
				if (linkedNode.getNext() == null) {
					if (linkedNode.getPrevious() == null) {
						header = null;
						tail = null;
					} else {
						linkedNode.getPrevious().setNext(null);
						tail = linkedNode.getPrevious();
					}
				} else {
					if (linkedNode.getPrevious() == null) {
						linkedNode.getNext().setPrevious(null);
						header = linkedNode.next;
					} else {
						linkedNode.getNext().setPrevious(
								linkedNode.getPrevious());
						linkedNode.getPrevious().setNext(linkedNode.getNext());
					}
				}
			}
		}
	}

	static class LinkedNode {
		private LinkedNode previous;
		private LinkedNode next;
		private DataNode data;

		public LinkedNode(DataNode data, LinkedNode nextNode,
				LinkedNode previousNode) {
			this.data = data;
			this.next = nextNode;
			this.previous = previousNode;
		}

		public LinkedNode getPrevious() {
			return previous;
		}

		public void setPrevious(LinkedNode previous) {
			this.previous = previous;
		}

		public LinkedNode getNext() {
			return next;
		}
		
		public void setNext(LinkedNode next) {
			this.next = next;
		}

		public DataNode getData() {
			return data;
		}

		public void setData(DataNode data) {
			this.data = data;
		}
	}

}
