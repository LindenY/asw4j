package ca.uwaterloo.asw.meta;

public @interface RequireData {
	
	String[] names();
	Class<?> types();
}
