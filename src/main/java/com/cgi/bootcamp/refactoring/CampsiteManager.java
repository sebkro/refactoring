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
	
	private int tentBase;
	private int cabinBase;
	private int caravanBase;
	
	private int lastAvailableStart;
	
	@Builder.Default public Map<LocalDate, WeeklyBooking> bookings = new HashMap<>();
	
	
	public void book (boolean tent, boolean cabin, boolean caravan, LocalDate startDate) {
		checkStartDay(startDate);
		WeeklyBooking weekBookings = bookings.getOrDefault(startDate, new WeeklyBooking());
		if (tent) {
			if (weekBookings.getTents() >= tents) {
				throw new IllegalArgumentException("no tents available");
			}
			weekBookings.addTent();
			bookings.put(startDate, weekBookings);
		} else if (caravan) {
			if (weekBookings.getCaravans() >= caravans) {
				throw new IllegalArgumentException("no caravans available");
			}
			weekBookings.addCaravan();
			bookings.put(startDate, weekBookings);
		} else if (cabin) {
			if (weekBookings.getCabins() >= cabins) {
				throw new IllegalArgumentException("no cabins available");
			}
			weekBookings.addCabin();
			bookings.put(startDate, weekBookings);
		}
	}

	private void checkStartDay(LocalDate startDate) {
		if (startDate.getDayOfWeek() != DayOfWeek.SATURDAY) {
			throw new IllegalArgumentException("start is not a saturday");
		}
	}
	
	public double calc(boolean tent, boolean cabin, boolean caravan, LocalDate startDate) {
		checkStartDay(startDate);
		double x = 0;
		if (tent) {
			if (bookings.get(startDate) != null && bookings.get(startDate).getTents() >= tents) {
				x = Double.NaN;
			} else {
				x = tentBase;
				if (bookings.get(startDate) != null && tents - bookings.get(startDate).getTents() <= lastAvailableStart) {
					double factor = 1 + ((lastAvailableStart - (tents - bookings.get(startDate).getTents()) + 1.0) / lastAvailableStart);
					x = factor * x;
				}
			}
		} else if (caravan) {
			if (bookings.get(startDate) != null && bookings.get(startDate).getCaravans() >= caravans) {
				x = Double.NaN;
			} else {
				x = caravanBase;
				if (bookings.get(startDate) != null && caravans - bookings.get(startDate).getCaravans() <= lastAvailableStart) {
					double fuzzyFactor = 1 + ((lastAvailableStart - (caravans - bookings.get(startDate).getCaravans()) + 1.0) / lastAvailableStart);
					x = fuzzyFactor * x;
				}
			}
		} else {
			if (bookings.get(startDate) != null && bookings.get(startDate).getCabins() >= cabins) {
				x = Double.NaN;
			} else {
				x = cabinBase;
			}
		}
		 
		if (startDate.getMonthValue() >= 5 && startDate.getMonthValue() <= 8) {
			x *= 1.5;
		}
		return x;
	}

}
