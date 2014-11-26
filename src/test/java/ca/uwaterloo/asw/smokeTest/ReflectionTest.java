package ca.uwaterloo.asw.smokeTest;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import ca.uwaterloo.asw.reflection.*;

public class ReflectionTest {

	@Test
	public void testEqualityWithNormalType() {
		
		Date d0 = new Date();
		Date d1 = new Date();
		String s0 = "String0";
		String s1 = "String1";
		
		TypeToken<?> d0Token = TypeToken.get(d0.getClass(), null);
		TypeToken<?> d1Token = TypeToken.get(d1.getClass(), null);
		TypeToken<?> s0Token = TypeToken.get(s0.getClass(), null);
		TypeToken<?> s1Token = TypeToken.get(s1.getClass(), null);
	}

	@Test
	public void testEqualityWithNormalTypeAndName() {
		
		Date d0 = new Date();
		Date d1 = new Date();
		Date d2 = new Date();
		String s0 = "String0";
		String s1 = "String1";
		String s2 = "String3";
		
		TypeToken<?> d0Token = TypeToken.get(d0.getClass(), "d");
		TypeToken<?> d1Token = TypeToken.get(d1.getClass(), "d");
		TypeToken<?> d2Token = TypeToken.get(d2.getClass(), "dd");
		TypeToken<?> s0Token = TypeToken.get(s0.getClass(), "s");
		TypeToken<?> s1Token = TypeToken.get(s1.getClass(), "s");
		TypeToken<?> s2Token = TypeToken.get(s2.getClass(), "ss");
		
		/*System.out.println("Compare d0 and d1 : " + d0Token.equals(d1Token));
		System.out.println("Compare d0 and d2 : " + d0Token.equals(d2Token));
		System.out.println("Compare d0 and s0 : " + d0Token.equals(s0Token));
		
		System.out.println("Compare s0 and s1 : " + s0Token.equals(s1Token));
		System.out.println("Compare s0 and s2 : " + s0Token.equals(s2Token));
		System.out.println("Compare s0 and d0 : " + s0Token.equals(d0Token));*/
	}

	// TODO: Solving GenericType is taking to much time. Lets think about it
	// later.
	@Ignore
	@Test
	public void testEqualityWithGenericType() {
	}

	@Ignore
	@Test
	public void testEqualityWithGenericTypeAndName() {

	}
}
