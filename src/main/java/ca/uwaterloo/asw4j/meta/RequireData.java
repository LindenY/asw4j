package ca.uwaterloo.asw4j.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireData {
	
	String[] names();
	Class<?>[] types() default {Object.class};
}
