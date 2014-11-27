package ca.uwaterloo.asw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.uwaterloo.asw.meta.DependOn;
import ca.uwaterloo.asw.meta.RequireData;
import ca.uwaterloo.asw.reflection.TypeToken;

public abstract class AbstractInstructionResolver implements
		InstructionResolver {

	protected ToolResolver toolResolver;
	protected DataStore dataStore;

	public AbstractInstructionResolver(ToolResolver toolResolver,
			DataStore dataStore) {
		this.toolResolver = toolResolver;
		this.dataStore = dataStore;
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	protected class InstructionNode {

		private List<Class<? extends Instruction<?, ?>>> dependencies;
		private List<TypeToken<?>> requireDatas;
		private TypeToken<?> produceData;

		public InstructionNode(
				Class<? extends Instruction<?, ?>> instructionClass) {

			DependOn dependOn = instructionClass.getAnnotation(DependOn.class);
			if (dependOn != null) {
				Class<? extends Instruction<?, ?>>[] depends = dependOn
						.instruction();
				if (depends != null) {
					dependencies = Arrays.asList(depends);
				}
			}
			dependencies = dependencies == null ? new ArrayList<Class<? extends Instruction<?,?>>>() : dependencies;
			
			
		}

		public List<Class<? extends Instruction<?, ?>>> getDependencies() {
			return dependencies;
		}

		public List<TypeToken<?>> getRequireDatas() {
			return requireDatas;
		}

		public TypeToken<?> getProduceData() {
			return produceData;
		}

	}
}
