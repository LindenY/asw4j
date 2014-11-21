package ca.uwaterloo.asw;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uwaterloo.asw.DataNode.STAGE;

public class SimpleInstructionResolver extends AbstractInstructionResolver {

	private final static Logger LOG = LoggerFactory
			.getLogger(SimpleInstructionResolver.class);

	private Map<Class, String[]> classDependenciesMap = new HashMap<Class, String[]>();

	public SimpleInstructionResolver(ToolResolver toolResolver,
			DataNodeStore dataNodeStore) {
		super(toolResolver, dataNodeStore);
	}

	public void registerInstruction(String[] requiredNames,
			Class instructionClass) {
		classDependenciesMap.put(instructionClass, requiredNames);
	}

	public int numberOfRegisteredInstruction() {
		return classDependenciesMap.keySet().size();
	}

	public Instruction resolveInstruction() {
		for (Class ic : classDependenciesMap.keySet()) {

			String[] requiredName = classDependenciesMap.get(ic);
			List<DataNode> requiredList = new ArrayList<DataNode>();
			DataNode matched = null;

			for (String name : requiredName) {
				Iterator<DataNode> iterator = dataNodeStore
						.iterator(STAGE.TRANSITIONAL);

				while (iterator.hasNext()) {
					DataNode next = iterator.next();
					if (next.getName().equals(name)) {
						
						LOG.warn("found matched : " + name);
						matched = next;
						break;
					}
				}

				if (matched == null) {
					break;
				} else {
					requiredList.add(matched);
					matched = null;
				}
			}

			if (requiredList.size() == requiredName.length) {
				return prepareInstructionClass(ic, requiredList);
			}
		}
		return null;
	}

	public Instruction getInstruction(String[] dataNodeNames) {
		// TODO Auto-generated method stub
		return null;
	}

	public void beforInstructionExecution(Instruction instruction) {
		// TODO Auto-generated method stub

	}

	public void afterInstructionExecution(Instruction instruction) {
		// TODO Auto-generated method stub
	}

	private Instruction prepareInstructionClass(Class instructionClass,
			List<DataNode> dataNodes) {

		Instruction instruction = instantizeInstruction(instructionClass);

		if (instruction == null) {
			return null;
		}
		
		instruction.setToolbox(toolResolver);

		DataNode[] toBeInjected = new DataNode[dataNodes.size()];
		for (int i = 0; i < dataNodes.size(); i++) {
			toBeInjected[i] = dataNodeStore.getAndRemove(dataNodes.get(i)
					.getName(), STAGE.TRANSITIONAL);
			
			LOG.debug("DataStoreSize: " + dataNodeStore.getAllDataNodesWithStage(STAGE.TRANSITIONAL).size());
		}
		instruction.setRawDataNodes(toBeInjected);
		return instruction;
	}

	private Instruction instantizeInstruction(Class instructionClass) {
		Instruction instruction = null;
		try {
			Constructor<Instruction> contructor = instructionClass
					.getDeclaredConstructor(null);
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

}
