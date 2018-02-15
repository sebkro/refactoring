package com.cgi.bootcamp.refactoring.tdd;

public class RomanNumeralsConverter {

	public String convert(int toConvert) {
		String result = "";
		for (int i = 0; i < toConvert; i++) {
			result += "I";
		}
		return result;
	}

}
