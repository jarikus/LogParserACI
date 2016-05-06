package com.aci.jd2015.checkers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeadCheckerImpl implements HeadChecker {

	private final String headPattern = "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.[0-9]{4}\\s([01]\\d|2[0123]):([012345]\\d):([012345]\\d)\\.[0-9]{3}\\s";
	private final Pattern pattern = Pattern.compile(headPattern);  
    private Matcher matcher; 
	
	
	@Override
	public boolean check(String string) {
		 matcher = pattern.matcher(string);
		 boolean isMatch = matcher.find();
		 return isMatch;  
	}

}
