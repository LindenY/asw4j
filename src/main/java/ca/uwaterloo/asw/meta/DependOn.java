package ca.uwaterloo.asw.meta;

import ca.uwaterloo.asw.Instruction;

public @interface DependOn {

	Class<? extends Instruction<?, ?>> instruction();
}
