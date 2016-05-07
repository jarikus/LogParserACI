package com.aci.jd2015.match.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.aci.jd2015.match.Matcher;
import com.aci.jd2015.model.MessageString;
import com.aci.jd2015.model.MessageStringType;

public class DirectedGraphMatcher implements Matcher {

	@Override
	public List<MessageString> lookOver(List<MessageString> heads, List<MessageString> crcs, List<MessageString> all) {
		if (heads != null && crcs != null && all != null) {

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
								return result;
							}
						}
					}

				}
			}
		}
		return null;
	}




	private class ArrayProcessor {

		private final static int CRC_ = 4;

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

			List<MessageString> resul2ElementList = checkTwoElementMessage();
			if (resul2ElementList != null) {
				return resul2ElementList;
			} else if (toProcess.size() == 2) {
				return null;
			}

			matrixSize = toProcess.size() - 2;
			createAdjacencyMatrix();
			List<Integer> matchedSequence = bypass(matrix);
			if (matchedSequence != null) {
				List<MessageString> resultList = new ArrayList<>();
				resultList.add(head);
				for (Integer index : matchedSequence) {
					resultList.add(toProcess.get(index + 1));
				}
				resultList.add(crc);
				return resultList;
			} else {
				return null;
			}
		}

		private void createAdjacencyMatrix () {
			matrix = new int[matrixSize][matrixSize];
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
			List<Integer> resultSequence = checkThreeElementMessage();
			if (resultSequence != null) {
				return resultSequence;
			} else {
				for (int i = 0; i < matrixSize; i++) {
					List<Integer> sequence = new ArrayList<Integer>();
					sequence.add(i);
					resultSequence = recursiveBypass(sequence, i);
					if (resultSequence != null) {
						return resultSequence;
					}
				}
			}
			return null;
		}

		private List<MessageString> checkTwoElementMessage() {
			String md5FromMessageString = crc.getString().substring(CRC_);
			String md5Generated = DigestUtils.md5Hex(head.getString());
			if (md5FromMessageString.equals(md5Generated)) {
				List<MessageString> resultList = new ArrayList<>();
				resultList.add(head);
				resultList.add(crc);
				return resultList;
			} else {
				return null;
			}
		}

		private List<Integer> checkThreeElementMessage(){
			List<Integer> sequence = new ArrayList<Integer>();
			for (int i = 0; i < matrixSize; i++) {
				sequence.add(i);
				boolean isValid = isValidMessage(sequence);
				if (isValid) {
					return sequence;
				} else {
					sequence.clear();
				}
			}
			return null;
		}

		private List<Integer> recursiveBypass(List<Integer> currentSequence, int begin) {
			for (int j = begin + 1; j < matrixSize; j++) {
				if (matrix[begin][j] == 1) {
					currentSequence.add(j);
					boolean isValid = isValidMessage(currentSequence);
					if (isValid) {
						return currentSequence;
					} else {
						List<Integer> copy = new ArrayList<>(currentSequence);
						copy = recursiveBypass(copy, j);
						if (copy == null) {
							currentSequence.remove(new Integer(j));
							continue;
						} else {
							return copy;
						}
					}
				}
			}
			return null;
		}

		private boolean isValidMessage(List<Integer> sequence) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(head.getString());
			for (Integer i : sequence) {
				stringBuilder.append(toProcess.get(i + 1).getString());
			}
			String md5FromMessageString = crc.getString().substring(CRC_);
			String md5Generated = DigestUtils.md5Hex(stringBuilder.toString());
			if (md5FromMessageString.equals(md5Generated)) {
				return true;
			} else {
				return false;
			}
		}

	}

}
