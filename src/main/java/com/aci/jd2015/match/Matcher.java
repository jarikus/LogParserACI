package com.aci.jd2015.match;

import java.util.List;

import com.aci.jd2015.model.MessageString;

public interface Matcher {
	List<MessageString> lookOver (List<MessageString> heads, List<MessageString> crcs, List<MessageString> crcsAndPlain);
}
