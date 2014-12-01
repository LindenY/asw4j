package ca.uwaterloo.asw.testObjects;

import java.util.Date;

import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.ToolResolver;

public class TestInstruction0 extends Instruction<Date, String> {

	public TestInstruction0(ToolResolver toolResolver) {
		super(toolResolver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void preExecution() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String execute(Date requiredData) {
		
		System.out.println("Execute");
		
		System.out.println("If requireData is null:" + (requiredData == null));
		
		return requiredData.toString();
	}

	@Override
	public void postExecution() {
		// TODO Auto-generated method stub
		
	}

}
