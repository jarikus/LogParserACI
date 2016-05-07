package com.aci.jd2015.checkers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HeadCheckerImplTest {

	private HeadChecker headChecker = new HeadCheckerImpl();
	
	@Test
	public void testHead1() {
		assertFalse(headChecker.check("XXX"));
	}
	
	@Test
	public void testHead2() {
		assertFalse(headChecker.check("28.03.2015 01:31:28 "));
	}
	
	@Test
	public void testHead3() {
		assertFalse(headChecker.check("28.3.2015 1:31:28.025 "));
	}
	
	@Test
	public void testHead4() {
		assertFalse(headChecker.check("03.28.2015 01:31:28.025 "));
	}
	

	@Test
	public void testHead5() {
		assertFalse(headChecker.check("2015.03.28 01:31:28.025 "));
	}
	
	@Test
	public void testHead6() {
		assertTrue(headChecker.check("28.03.2015 01:31:28.025 "));
	}
}
