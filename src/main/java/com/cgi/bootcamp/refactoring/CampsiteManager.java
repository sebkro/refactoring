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
	
	@Builder.Default public Map<LocalDate, int[]> bookings = new HashMap<>();
	
	
	public void book (boolean tent, boolean cabin, boolean caravan, LocalDate d) {
		if (d.getDayOfWeek() != DayOfWeek.SATURDAY) {
			throw new IllegalArgumentException("start is not a saturday");
		}
		int[] def = {0, 0, 0};
		int[] act = bookings.getOrDefault(d, def);
		if (tent) {
			if (act[2] >= tents) {
				throw new IllegalArgumentException("no tents available");
			}
			act[2] = act[2] + 1;
			bookings.put(d, act);
		} else if (caravan) {
			if (act[1] >= caravans) {
				throw new IllegalArgumentException("no caravans available");
			}
			act[1] = act[1] + 1;
			bookings.put(d, act);
		} else if (cabin) {
			if (act[0] >= cabins) {
				throw new IllegalArgumentException("no cabins available");
			}
			act[0] = act[0] + 1;
			bookings.put(d, act);
		}
	}
	
	public double calc(boolean tent, boolean cabin, boolean caravan, LocalDate date) {
		if (date.getDayOfWeek() != DayOfWeek.SATURDAY) {
			throw new IllegalArgumentException("start is not a saturday");
		}
		double x = 0;
		if (tent) {
			if (bookings.get(date) != null && bookings.get(date)[2] >= tents) {
				x = Double.NaN;
			} else {
				x = tentBase;
				if (bookings.get(date) != null && tents - bookings.get(date)[2] <= lastAvailableStart) {
					double factor = 1 + ((lastAvailableStart - (tents - bookings.get(date)[2]) + 1.0) / lastAvailableStart);
					x = factor * x;
				}
			}
		} else if (caravan) {
			if (bookings.get(date) != null && bookings.get(date)[1] >= caravans) {
				x = Double.NaN;
			} else {
				x = caravanBase;
				if (bookings.get(date) != null && caravans - bookings.get(date)[1] <= lastAvailableStart) {
					double fuzzyFactor = 1 + ((lastAvailableStart - (caravans - bookings.get(date)[1]) + 1.0) / lastAvailableStart);
					x = fuzzyFactor * x;
				}
			}
		} else {
			if (bookings.get(date) != null && bookings.get(date)[0] >= cabins) {
				x = Double.NaN;
			} else {
				x = cabinBase;
			}
		}
		 
		if (date.getMonthValue() >= 5 && date.getMonthValue() <= 8) {
			x *= 1.5;
		}
		return x;
	}

}
