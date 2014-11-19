package ca.uwaterloo.asw;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uwaterloo.asw.DataNode.STAGE;

public class SimpleInstructionResolver implements InstructionResolver {

	private final static Logger LOG = LoggerFactory.getLogger(SimpleInstructionResolver.class);
	
	private DependencyTree dependencyTree = new DependencyTree();
	private Comparator<DataNode> dataNodeComparator = new DataNodeComparator();
	private int numOfRegisteredInstructions = 0;
	
	protected ToolResolver toolResolver;
	protected DataNodeStore<List<DataNode>> dataNodeStore;
	
	public SimpleInstructionResolver(ToolResolver toolResolver, DataNodeStore dataNodeStore) {
		this.toolResolver = toolResolver;
		this.dataNodeStore = dataNodeStore;
	}
	
	public <T extends Instruction> void registerInstruction(String[] requiredNames,
			Class<T> instructionClass) {

		List<String> namesList = new ArrayList<String>(
				Arrays.asList(requiredNames));
		Collections.sort(namesList);
		Class<T> previousData = dependencyTree.insertDependencies(
				namesList.toArray(requiredNames), instructionClass);
		
		if (previousData == null) {
			numOfRegisteredInstructions ++;
		}
	}

	public int numberOfRegisteredInstruction() {
		return numOfRegisteredInstructions;
	}

	public Instruction resolveInstruction() {

		List<DataNode> dataNodes = dataNodeStore.getAllDataNodesWithStage(STAGE.TRANSITIONAL);
		//Collections.sort(dataNodes, dataNodeComparator);
		
		int dataNodesSize = dataNodes.size();
		for (int i=0; i<dataNodesSize-1; i++) {
			
			List<String> requiredNamesList = new ArrayList<String>();
			DependencyNode currentNode = dependencyTree.root;
			for (int j=i; j<dataNodesSize; j++) {
				currentNode = currentNode.getChildNodeWithKey(dataNodes.get(j).getName());
				requiredNamesList.add(currentNode.getKey());
				
				if (currentNode.getValue() != null) {
					Instruction instruction = instantizeInstruction(currentNode.getValue());
					instruction.setToolbox(toolResolver);
					injectRawDataNodesToInstruction(instruction, requiredNamesList);
					return instruction;
				}
			}
		}
		return null;
	}

	public Instruction getInstruction(String[] dataNodeNames) {
		return null;
	}

	public void beforInstructionExecution(Instruction instruction) {
	}

	public void afterInstructionExecution(Instruction instruction) {
	}

	private <T extends Instruction> T instantizeInstruction(
			Class<T> instructionClass) {

		T instruction = null;
		try {
			Constructor<T> contructor = instructionClass.getDeclaredConstructor(null);
			instruction = contructor.newInstance(null);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return instruction;
	}

	private void injectRawDataNodesToInstruction(
			Instruction instruction, List<String> requiredNames) {
		
		DataNode[] toBeInjected = new DataNode[requiredNames.size()];
		for (int i=0; i<requiredNames.size(); i++) {
			toBeInjected[i] = dataNodeStore.get(requiredNames.get(i), STAGE.TRANSITIONAL);
		}
		
		instruction.setRawDataNodes(toBeInjected);
	}
	
	private class DataNodeComparator implements Comparator<DataNode> {
		public int compare(DataNode o1, DataNode o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}

	private class DependencyTree {

		private DependencyNode root;

		public DependencyTree() {
			root = new DependencyNode("Root");
		}

		public <T extends Instruction> Class<T> insertDependencies(String[] dependencies,
				Class<T> value) {
			DependencyNode currentNode = root;
			for (String dependency : dependencies) {
				currentNode = currentNode.insertChild(dependency);
			}
			Class<T> previousValue = currentNode.getValue();
			currentNode.setValue(value);
			return previousValue;
		}

		public Class<Instruction> getValue(String[] dependencies)
				throws Exception {

			DependencyNode currentNode = root;
			for (String dependency : dependencies) {
				currentNode = currentNode.getChildNodeWithKey(dependency);

				if (currentNode == null) {
					return null;
				}

				if (currentNode.getValue() != null) {
					return currentNode.getValue();
				}
			}

			throw new Exception("Value not found");
		}
	}

	private static class DependencyNode {

		private Set<DependencyNode> children;

		private String key;
		private Class value;

		public DependencyNode(String key) {
			this.key = key;
		}

		public DependencyNode getChildNodeWithKey(String key) {
			if (children == null) {
				return null;
			}
			for (DependencyNode n : children) {
				if (n.getKey().equals(key)) {
					return n;
				}
			}
			return null;
		}

		public String getKey() {
			return key;
		}

		public <T extends Instruction> Class<T> getValue() {
			return value;
		}

		public <T extends Instruction> void setValue(Class<T> value) {
			this.value = value;
		}

		public <T extends Instruction> void insertChild(String key, Class<T> value) {
			DependencyNode ecn = insertChild(key);
			ecn.setValue(value);
		}

		public DependencyNode insertChild(String childKey) {
			DependencyNode ecn = this.getChildNodeWithKey(childKey);
			if (ecn == null) {
				if (children == null) {
					children = new HashSet<SimpleInstructionResolver.DependencyNode>();
				}
				ecn = new DependencyNode(childKey);
				children.add(ecn);
			}
			return ecn;
		}
	}
}
