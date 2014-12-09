package ca.uwaterloo.asw.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.ToolResolver;
import ca.uwaterloo.asw.reflection.TypeToken;

public class InstructionClassNode implements InstructionClassInfo, InstructionClassOmitter {

	protected final Class<? extends Instruction<?, ?>> instructionClass;
	protected final List<TypeToken<?>> requireDatas;
	protected final TypeToken<?> produceData;
	protected final boolean supportSingleton;
	
	protected STATE state;
	protected AtomicInteger numOfInstructionIssued;
	
	protected List<Instruction<?, ?>> pool;
	
	public InstructionClassNode(Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, 
			Type[] requireDataTypes,
			String produceDataName,
			Type produceDataType,
			boolean supportSingleton,
			boolean enablePooling
			) {
		
		this.instructionClass = instructionClass;
		
		InstructionClassUtility.checkPreconditionOfRequireDatas(requireDataNames, requireDataTypes);
		this.requireDatas = new ArrayList<TypeToken<?>>();
		for (int i = 0; i < requireDataTypes.length; i++) {
			this.requireDatas.add(TypeToken
					.get(requireDataTypes[i], 
						(requireDataNames == null || requireDataNames.length <= i 
							? null
							: requireDataNames[i])));
		}
		
		this.produceData = TypeToken.get(produceDataType, produceDataName);
		
		this.supportSingleton = supportSingleton;
		
		this.numOfInstructionIssued = new AtomicInteger(0);
		
		if (enablePooling) {
			this.pool = new ArrayList<Instruction<?,?>>();
		}
	}

	public Instruction<?, ?> getInstanceOfInstruction(ToolResolver toolResolver)
			throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		
		Instruction<?, ?> instruction = null;
		if (pool != null && pool.size() > 0) {
			synchronized (pool) {
				instruction = pool.remove(0);
			}
		} else {
			Constructor<? extends Instruction<?, ?>> constructor = null;
			
			if (toolResolver == null) {
				constructor = instructionClass.getDeclaredConstructor();
				instruction = constructor.newInstance();
			} else {
				constructor = instructionClass.getDeclaredConstructor(ToolResolver.class);
				instruction = constructor.newInstance(toolResolver);
			}
		}
		
		numOfInstructionIssued.incrementAndGet();
		
		return instruction;
		
	}

	public Class<? extends Instruction<?, ?>> getInstructionClass() {
		return instructionClass;
	}

	public TypeToken<?> getProduceData() {
		return produceData;
	}

	public List<TypeToken<?>> getRequireDatas() {
		return requireDatas;
	}

	public boolean isSupportSingleton() {
		return supportSingleton;
	}

	public int numberOfInstructionIssued() {
		return numOfInstructionIssued.get();
	}

	public void returnInstanceOfInstruction(Instruction<?, ?> instruction) {
		
		if (pool != null) {
			synchronized (pool) {
				pool.add(instruction);
			}
		}
		
		numOfInstructionIssued.decrementAndGet();
	}
	
	public STATE getState() {
		return state;
	}
	
	public void setState(STATE state) {
		synchronized (this.state) {
			this.state = state;
		}
	}

}
