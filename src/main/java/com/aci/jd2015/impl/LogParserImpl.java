package com.aci.jd2015.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.aci.jd2015.LogParser;
import com.aci.jd2015.model.MessageString;
import com.aci.jd2015.model.MessageStringType;
import com.aci.jd2015.checkers.CrcChecker;
import com.aci.jd2015.checkers.CrcCheckerImpl;
import com.aci.jd2015.checkers.HeadChecker;
import com.aci.jd2015.checkers.HeadCheckerImpl;
import com.aci.jd2015.match.Matcher;
import com.aci.jd2015.match.impl.DirectedGraphMatcher;

public class LogParserImpl implements LogParser {

	private CrcChecker crcChecker = new CrcCheckerImpl();
	private HeadChecker headChecker = new HeadCheckerImpl();
	
	private Matcher matcher = new DirectedGraphMatcher();

	private List<MessageString> heads = new ArrayList<>();
	private List<MessageString> crcs = new ArrayList<>();
	private List<MessageString> all = new ArrayList<>();

	@Override
	public void process(InputStream is, OutputStream os) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
		
		String temp;
		MessageString messageString;

		while (true) {
			temp = bufferedReader.readLine();
			if (temp == null) {
				break;
			} else if (crcChecker.check(temp)){
				messageString = new MessageString(MessageStringType.CRC, temp);
			} else if (headChecker.check(temp)){
				messageString = new MessageString(MessageStringType.HEAD, temp);
			} else {
				messageString = new MessageString(MessageStringType.PLAIN, temp);
			}

			all.add(messageString);
			MessageStringType messageStringType = messageString.getType();
			
			if (messageStringType.equals(MessageStringType.HEAD)) {
				heads.add(messageString);
			} else if (messageStringType.equals(MessageStringType.CRC)) {
				crcs.add(messageString);
				
				String message = matcher.lookOver(heads, crcs, all);
				if (message != null) {
					bufferedWriter.write(message);
				}
			}
			
			
		}

	}

}
