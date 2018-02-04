package com.cgi.bootcamp.refactoring;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
	
public class CampsiteManagerTest {
	
	private CampsiteManager manager;
	
	@Before
	public void init() {
		manager = CampsiteManager.builder()
				.tents(53)
				.cabins(4)
				.caravans(71)
				.tentBase(21)
				.cabinBase(210)
				.caravanBase(150)
				.lastAvailableStart(30).build();
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
		
		Assert.assertEquals(manager.getTentBase() * 1.0, result, 0.1);
	}

	@Test
	public void shouldReturnNormalCabinPrice() {
		double result = manager.calc(false, true, false, LocalDate.of(2017, 3, 25));
		
		Assert.assertEquals(manager.getCabinBase() * 1.0, result, 0.1);
	}

	@Test
	public void shouldReturnNormalCaravanPrice() {
		double result = manager.calc(false, false, true, LocalDate.of(2017, 3, 25));
		
		Assert.assertEquals(manager.getCaravanBase() * 1.0, result, 0.1);
	}

	@Test
	public void shouldReturnPeakCaravanPrice() {
		double result = manager.calc(false, false, true, LocalDate.of(2017, 7, 22));
		
		Assert.assertEquals(manager.getCaravanBase() * 1.5, result, 0.1);
	}

	@Test
	public void shouldReturnPeakTentPrice() {
		double result = manager.calc(true, false, false, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(manager.getTentBase() * 1.5, result, 0.1);
	}

	@Test
	public void shouldReturnMaxLastAvailableTentPrice() {
		WeeklyBooking bookings = new WeeklyBooking(0, 0, manager.getTents() - 1);
		manager.getBookings().put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.getTentBase() * 2.0, result, 0.1);
	}

	@Test
	public void shouldReturnMinLastAvailableTentPrice() {
		WeeklyBooking bookings = new WeeklyBooking(0, 0, manager.getTents() - manager.getLastAvailableStart());
		manager.getBookings().put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.getTentBase() * (1 + (1.0  / manager.getLastAvailableStart())), result, 0.0001);
	}

	@Test
	public void shouldReturnLastNormalTentPrice() {
		WeeklyBooking bookings = new WeeklyBooking(0, 0, manager.getTents() - manager.getLastAvailableStart() - 1);
		manager.getBookings().put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.getTentBase(), result, 0.0001);
	}

	@Test
	public void shouldReturnFuzzyLastAvailableTentPrice() {
		WeeklyBooking bookings = new WeeklyBooking(0, 0, manager.getTents() - manager.getLastAvailableStart() + 10);
		manager.getBookings().put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.getTentBase() * (1 + (11.0  / manager.getLastAvailableStart())), result, 0.0001);
	}
		
	@Test
	public void shouldReturnPeakAndFuzzyLastAvailableTentPrice() {
		WeeklyBooking bookings = new WeeklyBooking(0, 0, manager.getTents() - manager.getLastAvailableStart() + 10);
		manager.getBookings().put(LocalDate.of(2017, 8, 19), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(1.5 * manager.getTentBase() * (1 + (11.0  / manager.getLastAvailableStart())), result, 0.0001);
	}
	
	@Test
	public void shouldReturnMinLastAvailableCaravanPrice() {
		WeeklyBooking bookings = new WeeklyBooking(0, manager.getCaravans() - manager.getLastAvailableStart(), 0);
		manager.getBookings().put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(false, false, true, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.getCaravanBase() * (1 + (1.0  / manager.getLastAvailableStart())), result, 0.0001);
	}
	
	@Test
	public void shouldReturnLastNormalCaravanPrice() {
		WeeklyBooking bookings = new WeeklyBooking(0, manager.getCaravans() - manager.getLastAvailableStart() - 1, 0);
		manager.getBookings().put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(false, false, true, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.getCaravanBase(), result, 0.0001);
	}
	
	@Test
	public void shouldReturnFuzzyLastAvailableCaravanPrice() {
		WeeklyBooking bookings = new WeeklyBooking(0, manager.getCaravans() - manager.getLastAvailableStart() + 10, 0);
		manager.getBookings().put(LocalDate.of(2017, 11, 11), bookings);
		
		double result = manager.calc(false, false, true, LocalDate.of(2017, 11, 11));
		
		Assert.assertEquals(manager.getCaravanBase() * (1 + (11.0  / manager.getLastAvailableStart())), result, 0.0001);
	}

	@Test
	public void shouldReturnPeakAndFuzzyLastAvailableCaravanPrice() {
		WeeklyBooking bookings = new WeeklyBooking(0, manager.getCaravans() - manager.getLastAvailableStart() + 10, 0);
		manager.getBookings().put(LocalDate.of(2017, 8, 19), bookings);
		
		double result = manager.calc(false, false, true, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(1.5 * manager.getCaravanBase() * (1 + (11.0  / manager.getLastAvailableStart())), result, 0.0001);
	}

	@Test
	public void shouldReturnNaNPriceIfNoFreeCaravansAvailable() {
		WeeklyBooking bookings = new WeeklyBooking(0, manager.getCaravans(), 0);
		manager.getBookings().put(LocalDate.of(2017, 8, 19), bookings);
		
		double result = manager.calc(false, false, true, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(Double.NaN, result, 0.0001);
	}

	@Test
	public void shouldReturnNaNPriceIfNoFreeCabinsAvailable() {
		WeeklyBooking bookings = new WeeklyBooking(manager.getCabins(), 0, 0);
		manager.getBookings().put(LocalDate.of(2017, 8, 19), bookings);
		
		double result = manager.calc(false, true, false, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(Double.NaN, result, 0.0001);
	}

	@Test
	public void shouldReturnNaNPriceIfNoFreeTentsAvailable() {
		WeeklyBooking bookings = new WeeklyBooking(0, 0, manager.getTents());
		manager.getBookings().put(LocalDate.of(2017, 8, 19), bookings);
		
		double result = manager.calc(true, false, false, LocalDate.of(2017, 8, 19));
		
		Assert.assertEquals(Double.NaN, result, 0.0001);
	}
	
	@Test
	public void shouldBookTent() {
		WeeklyBooking bookings = new WeeklyBooking(0, 0, 4);
		manager.getBookings().put(LocalDate.of(2017, 8, 19), bookings);
		
		manager.book(true, false, false, LocalDate.of(2017, 8, 19));

		WeeklyBooking expected = new WeeklyBooking(0, 0, 5);
		Assert.assertEquals(expected, manager.getBookings().get(LocalDate.of(2017, 8, 19)));
	}

	@Test
	public void shouldBookCaravan() {
		WeeklyBooking bookings = new WeeklyBooking(0, 42, 4);
		manager.getBookings().put(LocalDate.of(2017, 8, 19), bookings);
		
		manager.book(false, false, true, LocalDate.of(2017, 8, 19));
		
		WeeklyBooking expected = new WeeklyBooking(0, 43, 4);
		Assert.assertEquals(expected, manager.getBookings().get(LocalDate.of(2017, 8, 19)));
	}

	@Test
	public void shouldBookCabin() {
		WeeklyBooking bookings = new WeeklyBooking(1, 42, 4);
		manager.getBookings().put(LocalDate.of(2017, 8, 19), bookings);
		
		manager.book(false, true, false, LocalDate.of(2017, 8, 19));
		
		WeeklyBooking expected = new WeeklyBooking(2, 42, 4);
		Assert.assertEquals(expected, manager.getBookings().get(LocalDate.of(2017, 8, 19)));
	}

}
