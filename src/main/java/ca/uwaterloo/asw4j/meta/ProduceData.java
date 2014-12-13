package ca.uwaterloo.asw4j.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.uwaterloo.asw4j.DataNode;

@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProduceData {

	String name();
	//Class<?> type() default DataNode.class;
}
