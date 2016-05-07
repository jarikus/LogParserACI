package com.aci.jd2015.checkers;

import org.junit.Test;
import static org.junit.Assert.*;

public class CrcCheckerImplTest {
	
	private CrcChecker crcChecker = new CrcCheckerImpl();
	
	@Test
	public void testCrc1() {
		assertFalse(crcChecker.check(" CRC_"));
	}
	
	@Test
	public void testCrc2() {
		assertFalse(crcChecker.check("^CRC_"));
	}
	
	@Test
	public void testCrc3() {
		assertFalse(crcChecker.check("crc_"));
	}
	
	@Test
	public void testCrc4() {
		assertFalse(crcChecker.check("CRC"));
	}
	
	@Test
	public void testCrc5() {
		assertTrue(crcChecker.check("CRC_es wteqe4y ey s ywu wernyae6nenaerynew y aebt qery w"));
	}

}
