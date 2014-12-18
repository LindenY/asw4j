package ca.uwaterloo.asw4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uwaterloo.asw4j.internal.InstructionClassNode;
import ca.uwaterloo.asw4j.internal.InstructionClassState;
import ca.uwaterloo.asw4j.internal.InstructionClassUtility;

/**
 * <p>
 * An implementation of {@link AbstractInstructionResolver} which has minimal
 * overhead for {@link Instruction} classes. {@link Instruction}s are resolved
 * in the order of {@link Instruction}s register order.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
public class SimpleInstructionResolver extends AbstractInstructionResolver {
	
	private final static Logger LOG = LoggerFactory.getLogger(SimpleInstructionResolver.class);

	public SimpleInstructionResolver(DataStore dataStore) {
		this(null, dataStore, false);
	}

	public SimpleInstructionResolver(ToolResolver toolResolver,
			DataStore dataStore) {
		this(toolResolver, dataStore, false);
	}

	public SimpleInstructionResolver(ToolResolver toolResolver,
			DataStore dataStore, boolean enablePooling) {
		super(dataStore, toolResolver, enablePooling);
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass) {

		registerInstructionClass(instructionClass,
				InstructionClassUtility
						.getInstructionRequireDataNames(instructionClass),
				InstructionClassUtility
						.getInstructionRequireDataTypes(instructionClass),
				InstructionClassUtility
						.getInstructionProduceDataName(instructionClass),
				InstructionClassUtility
						.getInstructionSingletonSupport(instructionClass));
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String produceDataName) {

		registerInstructionClass(instructionClass,
				InstructionClassUtility
						.getInstructionRequireDataNames(instructionClass),
				InstructionClassUtility
						.getInstructionRequireDataTypes(instructionClass),
				produceDataName,
				InstructionClassUtility
						.getInstructionSingletonSupport(instructionClass));
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes) {

		registerInstructionClass(instructionClass, requireDataNames,
				requireDataTypes,
				InstructionClassUtility
						.getInstructionProduceDataName(instructionClass),
				InstructionClassUtility
						.getInstructionSingletonSupport(instructionClass));
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName) {

		registerInstructionClass(instructionClass, requireDataNames,
				requireDataTypes, produceDataName,
				InstructionClassUtility
						.getInstructionSingletonSupport(instructionClass));
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton) {

		putIntructionClassNode(new InstructionClassNode(instructionClass,
				requireDataNames, requireDataTypes, produceDataName,
				InstructionClassUtility
						.getInstructionProduceDataType(instructionClass),
				supportSingleton, enablePooling));
	}

	public Instruction<?, ?> resolveInstruction() throws NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Instruction<?, ?> instruction = null;
		
		Iterator<InstructionClassNode> iterator = instructionClassMap.values()
				.iterator();
		while (iterator.hasNext()) {

			InstructionClassNode nextIN = iterator.next();

			if (nextIN.getState() != InstructionClassState.Ready()) {
				continue;
			} else if (dataStore.containAll(nextIN.getRequireDatas())) {
				DataNode requireData = dataStore.getAndRemoveAll(nextIN
						.getRequireDatas());
				instruction = nextIN
						.getInstanceOfInstruction(toolResolver);
				instruction.setRequireData(requireData);
				return instruction;
			}
		}
		
		if (LOG.isTraceEnabled()) {
			for (InstructionClassNode n : instructionClassMap.values()) {
				LOG.trace(n.getInstructionClass().getName() + " State=" + n.getState());
			}
			LOG.trace("Resolved Instruction: " + (instruction == null ? null : instruction.getClass().getName()));
		}

		return instruction;
	}
}
