package ca.uwaterloo.asw4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.uwaterloo.asw4j.internal.InstructionClassDependencyNode;
import ca.uwaterloo.asw4j.internal.InstructionClassUtility;
import ca.uwaterloo.asw4j.internal.InstructionClassInfo.STATE;

/**
 * A {@link InstructionResolver} that resolves dependencies relationship between
 * {@link Instruction}s, and resolves {@link Instruction}s in the order of their
 * dependencies when invokes {@link #resolveInstruction()}.
 * 
 * 
 * @author Desmond Lin
 *
 */
public abstract class DAGInstructionResolver extends AbstractInstructionResolver {

	
	private boolean requireResolve;
	private List<MarkableDependencyNode> instructionClassNodes;
	private int numOfRegisteredInstruction;
	
	public DAGInstructionResolver(DataStore dataStore) {
		this(null, dataStore, true);
	}
	
	public DAGInstructionResolver(ToolResolver toolResolver, DataStore dataStore, boolean enablePooling) {
		super(dataStore, toolResolver, enablePooling);
		
		instructionClassNodes = new ArrayList<DAGInstructionResolver.MarkableDependencyNode>();
		numOfRegisteredInstruction = 0;
	}

	public int numberOfRegisteredInstruction() {
		return numOfRegisteredInstruction;
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
		
		for (Class<? extends Instruction<?, ?>> d : mdn.getDependencies()) {
			registerInstructionClass(d);
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
		}
		
		Instruction<?, ?> instruction = null;
		
		Iterator<MarkableDependencyNode> iterator = instructionClassNodes.iterator();
		while (iterator.hasNext()) {
			MarkableDependencyNode nextDN = iterator.next();
			
			if (nextDN.getState() == STATE.Blocking
					|| nextDN.getState() == STATE.Terminated) {
				continue;
			} else {

				if (dataStore.containAll(nextDN.getRequireDatas())) {

					nextDN.setState(STATE.Running);
					instruction = nextDN
							.getInstanceOfInstruction(toolResolver);
					instruction.setRequireData(dataStore.getAndRemoveAll(nextDN
							.getRequireDatas()));
					break;
				} else {
					
					boolean isReady = false;
					for (InstructionClassDependencyNode dn : nextDN.getOutgoings()) {
						if (dn.getState() != STATE.Terminated) {
							isReady = true;
							break;
						}
					}
					
					if (nextDN.numberOfInstructionIssued() <= 0  && !isReady) {
						nextDN.setState(STATE.Terminated);
					}
				}
			}
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
			for (InstructionClassDependencyNode outgoing : dependencyNode.getOutgoings()) {
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
		public void setState(STATE state) {
			this.state = state;

			InstructionClassDependencyNode parent = null;
			
			if (getIncomings() != null) {
				for (InstructionClassDependencyNode idn : getIncomings()) {
					parent = idn;
					break;
				}
			}
			
			if (parent != null && !parent.isSupportAsync()) {
				if (state != STATE.Terminated) {
					parent.setState(STATE.Blocking);
				} else {
					boolean isReady = true;

					for (InstructionClassDependencyNode child : parent.getOutgoings()) {
						if (child.getState() != STATE.Terminated) {
							isReady = false;
							break;
						}
					}
					
					if (isReady) {
						parent.setState(STATE.Ready);
					}
				}
			}
		}
	}
}