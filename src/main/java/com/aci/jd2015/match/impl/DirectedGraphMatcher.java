package com.aci.jd2015.match.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.aci.jd2015.match.Matcher;
import com.aci.jd2015.model.MessageString;
import com.aci.jd2015.model.MessageStringType;

public class DirectedGraphMatcher implements Matcher {

	@Override
	public String lookOver(List<MessageString> heads, List<MessageString> crcs, List<MessageString> all) {
		Iterator<MessageString> headIterator = heads.iterator();
		while (headIterator.hasNext()) {
			MessageString messageStringHead = headIterator.next();
			int headIndex = all.indexOf(messageStringHead);
			if (headIndex == -1) {
				return null;
			} else {
				
				Iterator<MessageString> crcIterator = crcs.iterator();
				while (crcIterator.hasNext()) {
					MessageString messageStringCrc = crcIterator.next();
					int crcIndex = all.indexOf(messageStringCrc);
					if (headIndex < crcIndex) {
						List<MessageString> toSortOut = new ArrayList<>();
						toSortOut.add(messageStringHead);
						for (int i = headIndex + 1; i < crcIndex; i++) {
							MessageString temp = all.get(i);
							if (temp.getType().equals(MessageStringType.PLAIN)) {
								toSortOut.add(temp);
							}
						}
						toSortOut.add(messageStringCrc);

						ArrayProcessor arrayProcessor = new ArrayProcessor(toSortOut);
						List<MessageString> result = arrayProcessor.process();
						if (result != null) {
							headIterator.remove();
							crcIterator.remove();
							all.removeAll(result);
						}
						String resultMessage = generateResultMessage(result);
						return resultMessage;
					}
				}

			}
		}
		return null;
	}

	private String generateResultMessage(List<MessageString> result) {
		StringBuilder stringBuilder = new StringBuilder();
		for (MessageString messageString : result) {
			stringBuilder.append(messageString.getString()).append('\n');
		}
		stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("\n"));
		return stringBuilder.toString();
	}



	class ArrayProcessor {

		private List<MessageString> toProcess;
		private MessageString head;
		private MessageString crc;
		private int[] matrix[];
		private int matrixSize;

		public ArrayProcessor(List<MessageString> toSortOut) {
			this.toProcess = toSortOut;
			this.head = toSortOut.get(0);
			this.crc = toSortOut.get(toSortOut.size() - 1);
		}

		public List<MessageString> process() {

			List<Integer> sequence = new ArrayList<Integer>() {{
				add(new Integer(0));
			}};
			boolean validMessage = isValidMessage(sequence);
			if (validMessage) {
				List<MessageString> resultList = new ArrayList<>();
				resultList.add(head);
				resultList.add(crc);
				return resultList;
			}
			if (toProcess.size() == 2) {
				return null;
			}

			matrixSize = toProcess.size() - 2;
			createAdjacencyMatrix();
			List<Integer> matchedSequence = bypass(matrix);
			if (matchedSequence != null) {
				List<MessageString> resultList = new ArrayList<>();
				resultList.add(head);
				for (Integer index : matchedSequence) {
					resultList.add(toProcess.get(index));
				}
				resultList.add(crc);
				return resultList;
			}
			return null;
		}

		private void createAdjacencyMatrix () {
			matrix = new int[matrixSize][];
			for (int[] arr : matrix) {
				arr = new int[matrixSize];
			}
			for (int i = 0; i < matrixSize; i++) {
				for (int j = 0; j < matrixSize; j++) {
					if (i < j) {
						matrix[i][j] = 1;
					} else {
						matrix[i][j] = 0;
					}
				}
			}
		}

		private List<Integer> bypass(int[][] matrix) {
			for (int i = 0; i < matrixSize; i++) {
				List<Integer> resultSequence = recursiveBypass(new ArrayList<Integer>(), i);
				if (resultSequence != null) {
					return resultSequence;
				}
			}
			return null;
		}

		private List<Integer> recursiveBypass(List<Integer> currentSequence, int begin) {
			for (int j = 0; j < matrixSize; j++) {
				if ((begin == j) && (currentSequence.isEmpty())) {
					currentSequence.add(j);
					boolean isValid = isValidMessage(currentSequence);
					if (isValid) {
						return currentSequence;
					} else {
						currentSequence.clear();
					}
				}
				if (matrix[begin][j] == 1) {
					currentSequence.add(j);
					boolean isValid = isValidMessage(currentSequence);
					if (isValid) {
						return currentSequence;
					} else {
						List<Integer> copy = new ArrayList<>();
						Collections.copy(copy, currentSequence);
						return recursiveBypass(copy, j);
					}
				}
			}

			return null;
		}

		private boolean isValidMessage(List<Integer> sequence) {

			return false;
		}

	}

}
