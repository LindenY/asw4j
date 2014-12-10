package ca.uwaterloo.asw.testObjects;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ca.uwaterloo.asw4j.DataNode;
import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.ToolResolver;
import ca.uwaterloo.asw4j.meta.RequireData;

public class TestInstructionWithoutDependency {

	/**
	 * RequireData: [{@link Date}]
	 * ProduceData: [{@link String}]
	 */
	public static class InstructionWithoutAnnotation extends Instruction<Date, String> {

		public InstructionWithoutAnnotation(ToolResolver toolResolver) {
			super(toolResolver);
		}

		@Override
		public void preExecution() {
		}

		@Override
		public String execute(Date requireData) {
			return requireData.toString();
		}

		@Override
		public void postExecution() {
		}
	}
	
	
	/**
	 * RequireData: [{@link String}, 'str'], [{@link String}, 'str']
	 * ProduceData: [{@link String}]
	 */
	@RequireData(types={String.class, String.class}, names = {"str", "str"})
	public static class InstructionWithAnnotation extends Instruction<DataNode, String> {

		public InstructionWithAnnotation(ToolResolver toolResolver) {
			super(toolResolver);
		}

		@Override
		public void preExecution() {
		}

		@Override
		public String execute(DataNode requireData) {
			List<String> strList = requireData.getAll(String.class, "str");
			
			String result = "";
			for (String str : strList) {
				result += str;
			}
			return result;
		}

		@Override
		public void postExecution() {
		}
	}

	public static class InstructionWithoutParameterizedTypes extends Instruction {

		public InstructionWithoutParameterizedTypes(ToolResolver toolResolver) {
			super(toolResolver);
		}

		@Override
		public void preExecution() {
			
		}

		@Override
		public Object execute(Object requireData) {
			return null;
		}

		@Override
		public void postExecution() {
		}
		
	}
	
	public static class InstructionWithGenericType extends Instruction<List<String>, Map<String, Date>> {

		public InstructionWithGenericType(ToolResolver toolResolver) {
			super(toolResolver);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void preExecution() {
		}

		@Override
		public Map<String, Date> execute(List<String> requireData) {
			return null;
		}

		@Override
		public void postExecution() {
		}
		
	}
	
	@RequireData(types={Date.class, String.class}, names={"d"})
	public static class InstructionWithIncorrectRequireType extends Instruction<String, String> {

		public InstructionWithIncorrectRequireType(ToolResolver toolResolver) {
			super(toolResolver);
		}

		@Override
		public void preExecution() {
			
		}

		@Override
		public String execute(String requireData) {
			return null;
		}

		@Override
		public void postExecution() {

		}
		
	}

}
