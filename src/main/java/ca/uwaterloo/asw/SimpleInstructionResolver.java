package ca.uwaterloo.asw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import ca.uwaterloo.asw.internal.InstructionNode;

public class SimpleInstructionResolver extends AbstractInstructionResolver {

	private final boolean enableCache;
	private final Map<Class<? extends Instruction<?, ?>>, SimpleInstructionNode> instructionNodeMap;

	public SimpleInstructionResolver(ToolResolver toolResolver,
			DataStore dataStore) {
		this(toolResolver, dataStore, false);
	}

	public SimpleInstructionResolver(ToolResolver toolResolver,
			DataStore dataStore, boolean enableCache) {
		super(toolResolver, dataStore);
		this.enableCache = enableCache;
		this.instructionNodeMap = new HashMap<Class<? extends Instruction<?, ?>>, SimpleInstructionNode>();
	}

	public boolean isEnableCache() {
		return enableCache;
	}

	public void register(String[] requireDataNames,
			Class<?>[] requireDataTypes, String produceDataName,
			Class<?> produceDataType,
			Class<? extends Instruction<?, ?>> instructionClass) {

		instructionNodeMap.put(instructionClass, new SimpleInstructionNode(
				requireDataNames, requireDataTypes, produceDataName,
				produceDataType, instructionClass, enableCache));
	}

	public void register(Class<? extends Instruction<?, ?>> instructionClass) {

		String[] requireDataNames = InstructionNode
				.getInstructionRequireDataNames(instructionClass);
		Class<?>[] requireDataTypes = InstructionNode
				.getInstructionRequireDataTypes(instructionClass);
		String produceDataName = InstructionNode
				.getInstructionProduceDataName(instructionClass);
		Class<?> produceDataType = InstructionNode
				.getInstructionProduceDataType(instructionClass);

		register(requireDataNames, requireDataTypes, produceDataName,
				produceDataType, instructionClass);
	}

	public int numberOfRegisteredInstruction() {
		return instructionNodeMap.keySet().size();
	}

	public Instruction<?, ?> resolveInstruction() {
		
		Iterator<SimpleInstructionNode> iterator = instructionNodeMap.values()
				.iterator();
		while (iterator.hasNext()) {

			SimpleInstructionNode nextIN = iterator.next();
			
			if (nextIN.isSupportSingleton() && nextIN.issuedNum.get() > 0) {
				continue;
			} else if (dataStore.containAll(nextIN.getRequireDatas())) {
				DataNode requireData = dataStore.getAndRemoveAll(nextIN
						.getRequireDatas());
				Instruction<?, ?> instruction = nextIN
						.getInstructionInstance(toolResolver);
				instruction.setRequireData(requireData);
				return instruction;
			}
		}

		return null;
	}

	@Override
	public void afterInstructionExecution(Instruction<?, ?> instruction) {
		SimpleInstructionNode instructionNode = instructionNodeMap
				.get(instruction.getClass());

		instructionNode.returnInstruction(instruction);
	}

	private static class SimpleInstructionNode extends InstructionNode {

		private List<Instruction<?, ?>> pool;
		private AtomicInteger issuedNum;

		public SimpleInstructionNode(String[] requireDataNames,
				Class<?>[] requireDataTypes, String produceDataName,
				Class<?> produceDataType,
				Class<? extends Instruction<?, ?>> instructionClass,
				boolean enableCache) {

			super(requireDataNames, requireDataTypes, produceDataName,
					produceDataType, instructionClass);

			if (enableCache) {
				pool = new ArrayList<Instruction<?, ?>>();
			}
			
			issuedNum = new AtomicInteger(0);
		}

		public void returnInstruction(Instruction<?, ?> instruction) {

			issuedNum.decrementAndGet();

			if (pool == null) {
				return;
			}

			synchronized (pool) {
				pool.add(instruction);
			}
		}

		@Override
		public Instruction<?, ?> getInstructionInstance(
				ToolResolver toolResolver) {

			issuedNum.incrementAndGet();

			if (pool == null || pool.size() <= 0) {
				return super.getInstructionInstance(toolResolver);
			}

			synchronized (pool) {
				return pool.get(0);
			}
		}
	}
}
