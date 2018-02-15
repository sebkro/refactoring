package com.cgi.bootcamp.refactoring.tdd;

import org.junit.Assert;
import org.junit.Test;

public class RomanNumeralsConverterTest {
	
	
	@Test
	public void shouldConcvert1() {
		RomanNumeralsConverter converter = new RomanNumeralsConverter();
		String result = converter.convert(1);
		
		Assert.assertEquals("I", result);
	}

	@Test
	public void shouldConcvert2() {
		RomanNumeralsConverter converter = new RomanNumeralsConverter();
		String result = converter.convert(2);
		
		Assert.assertEquals("II", result);
	}

	@Test
	public void shouldConcvert3() {
		RomanNumeralsConverter converter = new RomanNumeralsConverter();
		String result = converter.convert(3);
		
		Assert.assertEquals("III", result);
	}
	
}
