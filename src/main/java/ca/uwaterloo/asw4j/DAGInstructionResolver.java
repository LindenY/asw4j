package ca.uwaterloo.asw4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uwaterloo.asw4j.internal.InstructionClassDependencyNode;
import ca.uwaterloo.asw4j.internal.InstructionClassNode;
import ca.uwaterloo.asw4j.internal.InstructionClassUtility;
import ca.uwaterloo.asw4j.internal.InstructionClassState;
import ca.uwaterloo.asw4j.meta.DependOn;
import ca.uwaterloo.asw4j.meta.Singleton;

/**
 * <p>
 * An implementation of {@link AbstractInstructionResolver} that resolves
 * dependencies relationship between {@link Instruction}s, and resolves
 * {@link Instruction}s in the order of their dependencies when invokes
 * {@link #resolveInstruction()}.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 *
 */
public class DAGInstructionResolver extends AbstractDependencyInstructionResolver {

	private final static Logger LOG = LoggerFactory
			.getLogger(DAGInstructionResolver.class);
	
	private List<MarkableDependencyNode> orderedInstructionClassNodes;

	public DAGInstructionResolver(DataStore dataStore) {
		this(dataStore, null, false);
	}
	
	public DAGInstructionResolver(DataStore dataStore, ToolResolver toolResolver) {
		this(dataStore, toolResolver, false);
	}

	public DAGInstructionResolver(DataStore dataStore,
			ToolResolver toolResolver, boolean enablePooling) {
		super(dataStore, toolResolver, enablePooling);
	}

	/**
	 * <p>
	 * Register an {@link Instruction} class.
	 * </p>
	 * <p>
	 * The depending {@link Instruction} classes of the registered
	 * {@link Instruction} are recursively registered.
	 * </p>
	 * 
	 * @param instructionClass
	 *            The {@link Instruction} class to be registered.
	 * @param requireDataNames
	 *            The require data types of the {@link Instruction} class.
	 * @param requireDataTypes
	 *            The require data names of the {@link Instruction} class.
	 * @param produceDataName
	 *            The produce data name of the {@link Instruction} class.
	 * @param supportSingleton
	 *            If the {@link Instruction} class is singleton. Default is
	 *            {@code false}.
	 * @param dependencies
	 *            The depending {@link Instruction} classes of the
	 *            {@link Instruction} class.
	 * @param supportAsync
	 *            If the {@link Instruction} class supports async property.
	 * 
	 * @see {@link Singleton} and {@link DependOn}
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton,
			Class<? extends Instruction<?, ?>>[] dependencies,
			boolean supportAsync) {

		MarkableDependencyNode mdn = new MarkableDependencyNode(
				instructionClass, requireDataNames, requireDataTypes,
				produceDataName,
				InstructionClassUtility
						.getInstructionProduceDataType(instructionClass),
				supportSingleton, enablePooling, dependencies, supportAsync);

		if (mdn.getDependencies() != null) {
			for (Class<? extends Instruction<?, ?>> d : mdn.getDependencies()) {
				registerInstructionClass(d);
			}
		}

		putIntructionClassNode(mdn);
		prepared = false;
	}

	public Instruction<?, ?> resolveInstruction() throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		
		prepare();

		Instruction<?, ?> instruction = null;

		Iterator<MarkableDependencyNode> iterator = orderedInstructionClassNodes
				.iterator();
		while (iterator.hasNext()) {
			MarkableDependencyNode nextDN = iterator.next();

			if (nextDN.getState() != InstructionClassState.Ready()) {
				continue;
			} else {

				if (dataStore.containAll(nextDN.getRequireDatas())) {
					instruction = nextDN.getInstanceOfInstruction(toolResolver);
					instruction.setRequireData(dataStore.getAndRemoveAll(nextDN
							.getRequireDatas()));
					break;
				} else {

					boolean isReady = false;
					for (InstructionClassDependencyNode dn : nextDN
							.getOutgoings()) {
						if (dn.getState() != InstructionClassState.Terminated()) {
							isReady = true;
							break;
						}
					}

					if (nextDN.numberOfInstructionIssued() <= 0 && !isReady) {
						nextDN.setState(InstructionClassState.Terminated());
					}
				}
			}
		}

		if (LOG.isTraceEnabled()) {
			for (MarkableDependencyNode d : orderedInstructionClassNodes) {
				LOG.trace(d.getInstructionClass().getName() + " State="
						+ d.getState());
			}
			LOG.trace("Resolved Instruction: "
					+ (instruction == null ? null : instruction.getClass()
							.getName()));
		}

		return instruction;
	}
	
	@Override
	protected void prepare() {
		if (prepared) {
			return;
		}
		
		super.prepare();
		resolveDependentsOrder();
	}
	
	private void resolveDependentsOrder() {
		orderedInstructionClassNodes = new ArrayList<DAGInstructionResolver.MarkableDependencyNode>();

		Iterator<InstructionClassNode>iterator = instructionClassMap.values().iterator();
		while (iterator.hasNext()) {
			MarkableDependencyNode nextNode = (MarkableDependencyNode) iterator.next();
			visitDependencyNode(nextNode, orderedInstructionClassNodes);
		}
	}

	private void visitDependencyNode(MarkableDependencyNode dependencyNode,
			List<MarkableDependencyNode> dependentOrder) {

		if (dependencyNode.getMark() == MarkableDependencyNode.MARK.unmark) {
			dependencyNode.mark(MarkableDependencyNode.MARK.temp);
			for (InstructionClassDependencyNode outgoing : dependencyNode
					.getIncomings()) {
				MarkableDependencyNode markable = (MarkableDependencyNode) outgoing;
				visitDependencyNode(markable, dependentOrder);
			}

			dependencyNode.mark(MarkableDependencyNode.MARK.marked);
			dependentOrder.add(0, dependencyNode);

		} else if (dependencyNode.getMark() == MarkableDependencyNode.MARK.temp) {
			throw new IllegalArgumentException(
					"There is a circle in dependencies. Hint:"
							+ dependencyNode.getInstructionClass().getName());
		}

	}

	private static class MarkableDependencyNode extends
			InstructionClassDependencyNode {

		public enum MARK {
			unmark, temp, marked
		}

		private MARK mark;

		public MarkableDependencyNode(
				Class<? extends Instruction<?, ?>> instructionClass,
				String[] requireDataNames, Type[] requireDataTypes,
				String produceDataName, Type produceDataType,
				boolean supportSingleton, boolean enablePooling,
				Class<? extends Instruction<?, ?>>[] dependencies,
				boolean supportAsync) {

			super(instructionClass, requireDataNames, requireDataTypes,
					produceDataName, produceDataType, supportSingleton,
					enablePooling, dependencies, supportAsync);

			mark = MARK.unmark;
		}

		public void mark(MARK mark) {
			this.mark = mark;
		}

		public MARK getMark() {
			return mark;
		}

		@Override
		public void clear() {
			mark = MARK.unmark;
			super.clear();
		}
	}
}
