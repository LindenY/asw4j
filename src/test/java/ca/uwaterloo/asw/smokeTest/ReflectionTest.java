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
		
		assertTrue(d0Token.equals(d1Token));
		assertTrue(s0Token.equals(s1Token));
		assertFalse(d0Token.equals(s0Token));
		assertFalse(d1Token.equals(s1Token));
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
		
		assertTrue(d0Token.equals(d1Token));
		assertFalse(d0Token.equals(d2Token));
		assertFalse(d0Token.equals(s0Token));
		
		assertTrue(s0Token.equals(s1Token));
		assertFalse(s0Token.equals(s2Token));
		assertFalse(s0Token.equals(d0Token));
	}

	// TODO: Solving GenericType is taking too much time. Lets think about it
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
