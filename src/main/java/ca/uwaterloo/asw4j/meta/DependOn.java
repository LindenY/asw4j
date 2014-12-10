package ca.uwaterloo.asw4j.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.uwaterloo.asw4j.Instruction;

@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DependOn {

	Class<? extends Instruction<?, ?>>[] instructions();
	boolean async() default true;
}
