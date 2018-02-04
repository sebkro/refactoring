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

	private void checkStartDay(LocalDate startDate) {
		if (startDate.getDayOfWeek() != DayOfWeek.SATURDAY) {
			throw new IllegalArgumentException("start is not a saturday");
		}
	}
	
	public double calc(boolean tent, boolean cabin, boolean caravan, LocalDate startDate) {
		checkStartDay(startDate);
		double x = 0;
		if (tent) {
			if (getBookings().get(startDate) != null && getBookings().get(startDate).getTents() >= getTents()) {
				x = Double.NaN;
			} else {
				x = getTentBase();
				if (getBookings().get(startDate) != null && getTents() - getBookings().get(startDate).getTents() <= getLastAvailableStart()) {
					double factor = 1 + ((getLastAvailableStart() - (getTents() - getBookings().get(startDate).getTents()) + 1.0) / getLastAvailableStart());
					x = factor * x;
				}
			}
		} else if (caravan) {
			if (getBookings().get(startDate) != null && getBookings().get(startDate).getCaravans() >= getCaravans()) {
				x = Double.NaN;
			} else {
				x = getCaravanBase();
				if (getBookings().get(startDate) != null && getCaravans() - getBookings().get(startDate).getCaravans() <= getLastAvailableStart()) {
					double fuzzyFactor = 1 + ((getLastAvailableStart() - (getCaravans() - getBookings().get(startDate).getCaravans()) + 1.0) / getLastAvailableStart());
					x = fuzzyFactor * x;
				}
			}
		} else {
			if (getBookings().get(startDate) != null && getBookings().get(startDate).getCabins() >= getCabins()) {
				x = Double.NaN;
			} else {
				x = getCabinBase();
			}
		}
		 
		if (startDate.getMonthValue() >= 5 && startDate.getMonthValue() <= 8) {
			x *= 1.5;
		}
		return x;
	}

	public void setBookings(Map<LocalDate, WeeklyBooking> bookings) {
		this.bookings = bookings;
	}

	private void setCabins(int cabins) {
		this.cabins = cabins;
	}

}
