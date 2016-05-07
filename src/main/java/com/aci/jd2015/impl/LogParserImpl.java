package com.aci.jd2015.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.aci.jd2015.LogParser;
import com.aci.jd2015.model.Message;
import com.aci.jd2015.model.MessageString;
import com.aci.jd2015.model.MessageStringType;
import com.aci.jd2015.checkers.CrcChecker;
import com.aci.jd2015.checkers.CrcCheckerImpl;
import com.aci.jd2015.checkers.HeadChecker;
import com.aci.jd2015.checkers.HeadCheckerImpl;
import com.aci.jd2015.match.Matcher;
import com.aci.jd2015.match.impl.DirectedGraphMatcher;

public class LogParserImpl implements LogParser {
	
	private static final int DATETIME_LENGHT = 23;
	private static final String DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss.SS";

	private CrcChecker crcChecker = new CrcCheckerImpl();
	private HeadChecker headChecker = new HeadCheckerImpl();	
	private Matcher matcher = new DirectedGraphMatcher();

	private List<MessageString> heads = new ArrayList<>();
	private List<MessageString> crcs = new ArrayList<>();
	private List<MessageString> all = new ArrayList<>();
	
	private List<Message> messages = new ArrayList<>();

	@Override
	public void process(InputStream is, OutputStream os) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		
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
				
				List<MessageString> result = matcher.lookOver(heads, crcs, all);
				if (result != null) {
					Message message = generateResultMessage(result);
					if (message != null) {
						messages.add(message);
						Collections.sort(messages);
					}
				}
			}
		}
		if (!messages.isEmpty()) {
			for (Message message : messages) {
				bufferedWriter.write(message.getMessage());
			}
		}
		bufferedReader.close();
		bufferedWriter.flush();
		bufferedWriter.close();
	}
	

	private Message generateResultMessage(List<MessageString> result) {
		StringBuilder stringBuilder = new StringBuilder();
		for (MessageString messageString : result) {
			stringBuilder.append(messageString.getString()).append('\n');
		}
		
		String datetimeString = result.get(0).getString().substring(0, DATETIME_LENGHT);
		SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
		Date datetime;
		try {
			datetime = format.parse(datetimeString);
			Message message = new Message(datetime, stringBuilder.toString());
			return message;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
