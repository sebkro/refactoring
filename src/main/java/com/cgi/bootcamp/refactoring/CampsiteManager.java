package com.cgi.bootcamp.refactoring;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CampsiteManager {
	
	private int tents;
	private int cabins;
	private int caravans;
	
	
	@Builder.Default
	private Map<LocalDate, WeeklyBooking> bookings = new HashMap<>();
	
	
	public void book (boolean tent, boolean cabin, boolean caravan, LocalDate startDate) {
		checkStartDay(startDate);
		WeeklyBooking weekBookings = getBookings().getOrDefault(startDate, new WeeklyBooking());
		if (tent) {
			if (weekBookings.getTents() >= getTents()) {
				throw new IllegalArgumentException("no tents available");
			}
			weekBookings.addTent();
			getBookings().put(startDate, weekBookings);
		} else if (caravan) {
			if (weekBookings.getCaravans() >= getCaravans()) {
				throw new IllegalArgumentException("no caravans available");
			}
			weekBookings.addCaravan();
			getBookings().put(startDate, weekBookings);
		} else if (cabin) {
			if (weekBookings.getCabins() >= getCabins()) {
				throw new IllegalArgumentException("no cabins available");
			}
			weekBookings.addCabin();
			getBookings().put(startDate, weekBookings);
		}
	}

	public void checkStartDay(LocalDate startDate) {
		if (startDate.getDayOfWeek() != DayOfWeek.SATURDAY) {
			throw new IllegalArgumentException("start is not a saturday");
		}
	}

	public void setBookings(Map<LocalDate, WeeklyBooking> bookings) {
		this.bookings = bookings;
	}

}
