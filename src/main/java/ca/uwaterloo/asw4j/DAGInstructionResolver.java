package ca.uwaterloo.asw4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uwaterloo.asw4j.internal.InstructionClassDependencyNode;
import ca.uwaterloo.asw4j.internal.InstructionClassUtility;
import ca.uwaterloo.asw4j.internal.InstructionClassState;

/**
 * <pre>
 * A {@link InstructionResolver} that resolves dependencies relationship between
 * {@link Instruction}s, and resolves {@link Instruction}s in the order of their
 * dependencies when invokes {@link #resolveInstruction()}.
 * </pre>
 * <p>
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 *
 */
public class DAGInstructionResolver extends AbstractInstructionResolver {
	
	private final static Logger LOG = LoggerFactory.getLogger(DAGInstructionResolver.class);
	
	private boolean requireResolve;
	private List<MarkableDependencyNode> instructionClassNodes;
	
	public DAGInstructionResolver(DataStore dataStore) {
		this(null, dataStore, true);
	}
	
	public DAGInstructionResolver(ToolResolver toolResolver, DataStore dataStore, boolean enablePooling) {
		super(dataStore, toolResolver, enablePooling);
		
		instructionClassNodes = new ArrayList<DAGInstructionResolver.MarkableDependencyNode>();
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
						.getInstructionSingletonSupport(instructionClass),
				InstructionClassUtility
						.getInstructionDependencies(instructionClass),
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
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
						.getInstructionSingletonSupport(instructionClass),
				InstructionClassUtility
						.getInstructionDependencies(instructionClass),
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes) {
		
		registerInstructionClass(instructionClass, requireDataNames,
				requireDataTypes,
				InstructionClassUtility
						.getInstructionProduceDataName(instructionClass),
				InstructionClassUtility
						.getInstructionSingletonSupport(instructionClass),
				InstructionClassUtility
						.getInstructionDependencies(instructionClass),
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName) {

		registerInstructionClass(instructionClass, requireDataNames,
				requireDataTypes, produceDataName,
				InstructionClassUtility
						.getInstructionSingletonSupport(instructionClass),
				InstructionClassUtility
						.getInstructionDependencies(instructionClass),
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton) {

		registerInstructionClass(instructionClass, requireDataNames,
				requireDataTypes, produceDataName, supportSingleton,
				InstructionClassUtility
						.getInstructionDependencies(instructionClass),
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
	}
	
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName,
			boolean supportSingleton,
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

		instructionClassNodes.add(mdn);
		putIntructionClassNode(mdn);
		requireResolveDependencies();
	}
	
	public void requireResolveDependencies() {
		requireResolve = true;
	}
	
	public Instruction<?, ?> resolveInstruction() throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if (requireResolve) {
			InstructionClassDependencyNode.resolveDependencies(instructionClassNodes);
			resolveDependentsOrder();
			requireResolve = false;
		}
		
		Instruction<?, ?> instruction = null;
		
		Iterator<MarkableDependencyNode> iterator = instructionClassNodes.iterator();
		while (iterator.hasNext()) {
			MarkableDependencyNode nextDN = iterator.next();
			
			if (nextDN.getState() != InstructionClassState.Ready()) {
				continue;
			} else {

				if (dataStore.containAll(nextDN.getRequireDatas())) {
					instruction = nextDN
							.getInstanceOfInstruction(toolResolver);
					instruction.setRequireData(dataStore.getAndRemoveAll(nextDN
							.getRequireDatas()));
					break;
				} else {
					
					boolean isReady = false;
					for (InstructionClassDependencyNode dn : nextDN.getOutgoings()) {
						if (dn.getState() != InstructionClassState.Terminated()) {
							isReady = true;
							break;
						}
					}
					
					if (nextDN.numberOfInstructionIssued() <= 0  && !isReady) {
						nextDN.setState(InstructionClassState.Terminated());
					}
				}
			}
		}
		
		if (LOG.isTraceEnabled()) {
			for (MarkableDependencyNode d : instructionClassNodes) {
				LOG.trace(d.getInstructionClass().getName() + " State=" + d.getState());
			}
			LOG.trace("Resolved Instruction: " + (instruction == null ? null : instruction.getClass().getName()));
		}
		
		return instruction;
	}

	private void resolveDependentsOrder() {
		List<MarkableDependencyNode> dependentsOrder 
			= new ArrayList<DAGInstructionResolver.MarkableDependencyNode>();
		
		Iterator<MarkableDependencyNode> iterator = instructionClassNodes.iterator();
		while(iterator.hasNext()) {
			MarkableDependencyNode nextNode = iterator.next();
			visitDependencyNode(nextNode, dependentsOrder);
		}
		
		instructionClassNodes = dependentsOrder;
		
	}
	
	private void visitDependencyNode(MarkableDependencyNode dependencyNode,
			List<MarkableDependencyNode> dependentOrder) {

		if (dependencyNode.getMark() == MarkableDependencyNode.MARK.unmark) {
			dependencyNode.mark(MarkableDependencyNode.MARK.temp);
			for (InstructionClassDependencyNode outgoing : dependencyNode.getIncomings()) {
				MarkableDependencyNode markable = (MarkableDependencyNode) outgoing;
				visitDependencyNode(markable, dependentOrder);
			}
			
			dependencyNode.mark(MarkableDependencyNode.MARK.marked);
			dependentOrder.add(0, dependencyNode);
			
		} else if (dependencyNode.getMark() == MarkableDependencyNode.MARK.temp) {
			throw new IllegalArgumentException(
					"There is a circle in dependencies. Hint:" + 
					dependencyNode.getInstructionClass().getName());
		}
		
	}
	
	private static class MarkableDependencyNode extends InstructionClassDependencyNode {
		
		public enum MARK {
			unmark,
			temp,
			marked
		}
		
		private MARK mark;
		
		public MarkableDependencyNode (
				Class<? extends Instruction<?, ?>> instructionClass,
				String[] requireDataNames, 
				Type[] requireDataTypes,
				String produceDataName,
				Type produceDataType,
				boolean supportSingleton,
				boolean enablePooling,
				Class<? extends Instruction<?, ?>>[] dependencies,
				boolean supportAsync ) {
			
			super(
					instructionClass,
					requireDataNames,
					requireDataTypes,
					produceDataName,
					produceDataType,
					supportSingleton,
					enablePooling,
					dependencies,
					supportAsync
				);
			
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
