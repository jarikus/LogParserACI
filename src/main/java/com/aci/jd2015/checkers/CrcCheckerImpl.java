package com.aci.jd2015.checkers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrcCheckerImpl implements CrcChecker {

	private final String headPattern = "^CRC_";
	private final Pattern pattern = Pattern.compile(headPattern);  
	private Matcher matcher; 

	@Override
	public boolean check(String string) {
		matcher = pattern.matcher(string);  
		boolean isMatch = matcher.find();
		return isMatch;
	}

}
