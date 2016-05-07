package com.aci.jd2015.match.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import com.aci.jd2015.match.Matcher;
import com.aci.jd2015.model.MessageString;
import com.aci.jd2015.model.MessageStringType;

public class DirectedGraphMatcherTest {
	
	private Matcher matcher = new DirectedGraphMatcher();
	
	private List<MessageString> heads = new ArrayList<>();
	private List<MessageString> crcs = new ArrayList<>();
	private List<MessageString> all = new ArrayList<>();
	
	@Test
	public void testMatching1() {
		List<MessageString> list = matcher.lookOver(null, null, null);
		assertNull(list);
	}
	
	@Test
	public void testMatching2() {
		List<MessageString> list = matcher.lookOver(heads, crcs, all);
		assertNull(list);
	}
	
}
