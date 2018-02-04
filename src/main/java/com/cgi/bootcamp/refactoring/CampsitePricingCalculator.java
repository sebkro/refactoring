package com.cgi.bootcamp.refactoring;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CampsitePricingCalculator {
	
	private int tentBasePrice;
	private int cabinBasePrice;
	private int caravanBasePrice;
	private int lastAvailableStart;
	
	public double calc(boolean tent, boolean cabin, boolean caravan, LocalDate startDate, CampsiteBookingManager campsiteManager) {
		campsiteManager.checkStartDay(startDate);
		double x = 0;
		if (tent) {
			if (campsiteManager.getBookings().get(startDate) != null && campsiteManager.getBookings().get(startDate).getTents() >= campsiteManager.getTents()) {
				x = Double.NaN;
			} else {
				x = tentBasePrice;
				if (campsiteManager.getBookings().get(startDate) != null && campsiteManager.getTents() - campsiteManager.getBookings().get(startDate).getTents() <= lastAvailableStart) {
					double factor = 1 + ((lastAvailableStart - (campsiteManager.getTents() - campsiteManager.getBookings().get(startDate).getTents()) + 1.0) / lastAvailableStart);
					x = factor * x;
				}
			}
		} else if (caravan) {
			if (campsiteManager.getBookings().get(startDate) != null && campsiteManager.getBookings().get(startDate).getCaravans() >= campsiteManager.getCaravans()) {
				x = Double.NaN;
			} else {
				x = caravanBasePrice;
				if (campsiteManager.getBookings().get(startDate) != null && campsiteManager.getCaravans() - campsiteManager.getBookings().get(startDate).getCaravans() <= lastAvailableStart) {
					double fuzzyFactor = 1 + ((lastAvailableStart - (campsiteManager.getCaravans() - campsiteManager.getBookings().get(startDate).getCaravans()) + 1.0) / lastAvailableStart);
					x = fuzzyFactor * x;
				}
			}
		} else {
			if (campsiteManager.getBookings().get(startDate) != null && campsiteManager.getBookings().get(startDate).getCabins() >= campsiteManager.getCabins()) {
				x = Double.NaN;
			} else {
				x = cabinBasePrice;
			}
		}
		 
		if (startDate.getMonthValue() >= 5 && startDate.getMonthValue() <= 8) {
			x *= 1.5;
		}
		return x;
	}

}
