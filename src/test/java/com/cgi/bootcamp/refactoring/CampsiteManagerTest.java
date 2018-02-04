package com.cgi.bootcamp.refactoring;
import java.time.LocalDate;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CampsiteManagerTest {
	
	private CampsiteManager manager;
	
	@Before
	public void init() {
		manager = new CampsiteManager();
		manager.tents = 53;
		manager.cabins = 4;
		manager.caravans = 71;
		
		manager.tentBase = 21;
		manager.cabinBase = 210;
		manager.caravanBase = 150;
		
		manager.lastAvailableStart = 30;
		
		manager.bookings = new HashMap<>();
	}

	@Test(expected = IllegalArgumentException.class)
	public void calcShouldThrowExceptionIfStartIsNotSaturday1() {
		manager.calc(true, false, false, LocalDate.of(2017, 6, 1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void calcShouldThrowExceptionIfStartIsNotSaturday2() {
		manager.calc(false, true, false, LocalDate.of(2017, 7, 11));
	}

	@Test(expected = IllegalArgumentException.class)
	public void bookShouldThrowExceptionIfStartIsNotSaturday1() {
		manager.book(true, false, false, LocalDate.of(2017, 6, 1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void bookShouldThrowExceptionIfStartIsNotSaturday2() {
		manager.book(false, true, false, LocalDate.of(2017, 7, 11));
	}
	
	@Test
	public void shouldReturnNormalTentPrice() {
		double result = manager.calc(true, false, false, LocalDate.of(2017, 3, 25));
		
		Assert.assertEquals(manager.tentBase * 1.0, result, 0.1);
	}

	@Test
	public void shouldReturnNormalCabinPrice() {
		double result = manager.calc(false, true, false, LocalDate.of(2017, 3, 25));
		
		Assert.assertEquals(manager.cabinBase * 1.0, result, 0.1);
	}

	@Test
	public void shouldReturnNormalCaravanPrice() {
		double result = manager.calc(false, false, true, LocalDate.of(2017, 3, 25));
		
		Assert.assertEquals(manager.caravanBase * 1.0, result, 0.1);
	}

	@Test
	public void shouldReturnPeakCaravanPrice() {
		double result = manager.calc(false, false, true, LocalDate.of(2017, 7, 22));
		
		Assert.assertEquals(manager.caravanBase * 1.5, result, 0.1);
	}

	@Test
	public void shouldReturnPeakTentPrice() {
		double result = manager.calc(true, false, false, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(manager.tentBase * 1.5, result, 0.1);
	}

	@Test
	public void shouldReturnMaxLastAvailableTentPrice() {
		int[] bookings = {0, 0, manager.tents - 1};
		manager.bookings.put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.tentBase * 2.0, result, 0.1);
	}

	@Test
	public void shouldReturnMinLastAvailableTentPrice() {
		int[] bookings = {0, 0, manager.tents - manager.lastAvailableStart};
		manager.bookings.put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.tentBase * (1 + (1.0  / manager.lastAvailableStart)), result, 0.0001);
	}

	@Test
	public void shouldReturnLastNormalTentPrice() {
		int[] bookings = {0, 0, manager.tents - manager.lastAvailableStart - 1};
		manager.bookings.put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.tentBase, result, 0.0001);
	}

	@Test
	public void shouldReturnFuzzyLastAvailableTentPrice() {
		int[] bookings = {0, 0, manager.tents - manager.lastAvailableStart + 10};
		manager.bookings.put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.tentBase * (1 + (11.0  / manager.lastAvailableStart)), result, 0.0001);
	}
		
	@Test
	public void shouldReturnPeakAndFuzzyLastAvailableTentPrice() {
		int[] bookings = {0, 0, manager.tents - manager.lastAvailableStart + 10};
		manager.bookings.put(LocalDate.of(2017, 8, 19), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(1.5 * manager.tentBase * (1 + (11.0  / manager.lastAvailableStart)), result, 0.0001);
	}
	
	@Test
	public void shouldReturnMinLastAvailableCaravanPrice() {
		int[] bookings = {0, manager.caravans - manager.lastAvailableStart, 0};
		manager.bookings.put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(false, false, true, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.caravanBase * (1 + (1.0  / manager.lastAvailableStart)), result, 0.0001);
	}
	
	@Test
	public void shouldReturnLastNormalCaravanPrice() {
		int[] bookings = {0, manager.caravans - manager.lastAvailableStart - 1, 0};
		manager.bookings.put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(false, false, true, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.caravanBase, result, 0.0001);
	}
	
	@Test
	public void shouldReturnFuzzyLastAvailableCaravanPrice() {
		int[] bookings = {0, manager.caravans - manager.lastAvailableStart + 10, 0};
		manager.bookings.put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(false, false, true, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.caravanBase * (1 + (11.0  / manager.lastAvailableStart)), result, 0.0001);
	}

	@Test
	public void shouldReturnPeakAndFuzzyLastAvailableCaravanPrice() {
		int[] bookings = {0, manager.caravans - manager.lastAvailableStart + 10, 0};
		manager.bookings.put(LocalDate.of(2017, 8, 19), bookings);
		
		double result = manager.calc(false, false, true, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(1.5 * manager.caravanBase * (1 + (11.0  / manager.lastAvailableStart)), result, 0.0001);
	}

	@Test
	public void shouldReturnNaNPriceIfNoFreeCaravansAvailable() {
		int[] bookings = {0, manager.caravans, 0};
		manager.bookings.put(LocalDate.of(2017, 8, 19), bookings);
		
		double result = manager.calc(false, false, true, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(Double.NaN, result, 0.0001);
	}

	@Test
	public void shouldReturnNaNPriceIfNoFreeCabinsAvailable() {
		int[] bookings = {manager.cabins, 0, 0};
		manager.bookings.put(LocalDate.of(2017, 8, 19), bookings);
		
		double result = manager.calc(false, true, false, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(Double.NaN, result, 0.0001);
	}

	@Test
	public void shouldReturnNaNPriceIfNoFreeTentsAvailable() {
		int[] bookings = {0, 0, manager.tents};
		manager.bookings.put(LocalDate.of(2017, 8, 19), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(Double.NaN, result, 0.0001);
	}
	
	@Test
	public void shouldBookTent() {
		int[] bookings = {0, 0, 4};
		manager.bookings.put(LocalDate.of(2017, 8, 19), bookings);
		
		manager.book(true, false, false, LocalDate.of(2017, 8, 19));

		int[] expected = {0, 0, 5};
		Assert.assertArrayEquals(expected, manager.bookings.get(LocalDate.of(2017, 8, 19)));
	}

	@Test
	public void shouldBookCaravan() {
		int[] bookings = {0, 42, 4};
		manager.bookings.put(LocalDate.of(2017, 8, 19), bookings);
		
		manager.book(false, false, true, LocalDate.of(2017, 8, 19));
		
		int[] expected = {0, 43, 4};
		Assert.assertArrayEquals(expected, manager.bookings.get(LocalDate.of(2017, 8, 19)));
	}

	@Test
	public void shouldBookCabin() {
		int[] bookings = {1, 42, 4};
		manager.bookings.put(LocalDate.of(2017, 8, 19), bookings);
		
		manager.book(false, true, false, LocalDate.of(2017, 8, 19));
		
		int[] expected = {2, 42, 4};
		Assert.assertArrayEquals(expected, manager.bookings.get(LocalDate.of(2017, 8, 19)));
	}

}
