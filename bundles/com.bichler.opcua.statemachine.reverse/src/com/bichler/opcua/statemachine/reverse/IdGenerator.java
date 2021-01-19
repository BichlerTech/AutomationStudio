package com.bichler.opcua.statemachine.reverse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IdGenerator {

	// characters used to generate ids
		private static List<String> characters = new ArrayList<>();
		static {
			characters.add("A");
			characters.add("B");
			characters.add("C");
			characters.add("D");
			characters.add("E");
			characters.add("F");
			characters.add("G");
			characters.add("H");
			characters.add("I");
			characters.add("J");
			characters.add("K");
			characters.add("L");
			characters.add("M");
			characters.add("N");
			characters.add("O");
			characters.add("P");
			characters.add("Q");
			characters.add("R");
			characters.add("S");
			characters.add("T");
			characters.add("U");
			characters.add("V");
			characters.add("W");
			characters.add("X");
			characters.add("Y");
			characters.add("Z");

			characters.add("a");
			characters.add("b");
			characters.add("c");
			characters.add("d");
			characters.add("e");
			characters.add("f");
			characters.add("g");
			characters.add("h");
			characters.add("i");
			characters.add("j");
			characters.add("k");
			characters.add("l");
			characters.add("m");
			characters.add("n");
			characters.add("o");
			characters.add("p");
			characters.add("q");
			characters.add("r");
			characters.add("s");
			characters.add("t");
			characters.add("u");
			characters.add("v");
			characters.add("w");
			characters.add("x");
			characters.add("y");
			characters.add("z");

			characters.add("1");
			characters.add("2");
			characters.add("3");
			characters.add("4");
			characters.add("5");
			characters.add("6");
			characters.add("7");
			characters.add("8");
			characters.add("9");
			characters.add("0");
		}
	
	// random numbers
	private Random r = new Random();
	// character set appended to generated ids
	private String idAppendix = null;
	// existing generated ids
	private List<String> generatedIds = null;
	

	public IdGenerator(int appendix) {
		this.generatedIds = new ArrayList<>();
		this.idAppendix = this.generate(appendix);
	}

	private String generate(int count) {
		boolean loop = true;
		String newId = "";
		while (loop) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < count; i++) {
				int index = r.nextInt(IdGenerator.characters.size() - 1);
				builder.append(IdGenerator.characters.get(index));
			}
			newId = builder.toString();
			// check if id already generated
			boolean idExist = this.generatedIds.contains(newId);
			loop = idExist;
		}
		

		return newId;
	}

	/**
	 * Generates a new unique id
	 * 
	 * @param count
	 * 			number of unique characters generated
	 * @return id
	 */
	public String buildId(int count) {
		String newId = generate(count);
		return "_" + newId + this.idAppendix;
	}
}
