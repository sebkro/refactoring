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

	public double calcTentPrice(LocalDate startDate, CampsiteBookingManager campsiteBookingManager) {
		campsiteBookingManager.checkStartDay(startDate);
		double x = 0;
		if (campsiteBookingManager.getWeeklyBooking(startDate).getTents() >= campsiteBookingManager.getTents()) {
			x = Double.NaN;
		} else {
			x = tentBasePrice;
			if (campsiteBookingManager.getTents()
					- campsiteBookingManager.getWeeklyBooking(startDate).getTents() <= lastAvailableStart) {
				double factor = 1 + ((lastAvailableStart - (campsiteBookingManager.getTents()
						- campsiteBookingManager.getBookings().get(startDate).getTents()) + 1.0) / lastAvailableStart);
				x = factor * x;
			}
		}
		if (startDate.getMonthValue() >= 5 && startDate.getMonthValue() <= 8) {
			x *= 1.5;
		}
		return x;
	}

	public double calcCaravanPrice(LocalDate startDate, CampsiteBookingManager campsiteBookingManager) {
		campsiteBookingManager.checkStartDay(startDate);
		double x = 0;
		if (campsiteBookingManager.getWeeklyBooking(startDate).getCaravans() >= campsiteBookingManager.getCaravans()) {
			x = Double.NaN;
		} else {
			x = caravanBasePrice;
			if (campsiteBookingManager.getCaravans()
					- campsiteBookingManager.getWeeklyBooking(startDate).getCaravans() <= lastAvailableStart) {
				double fuzzyFactor = 1 + ((lastAvailableStart - (campsiteBookingManager.getCaravans()
						- campsiteBookingManager.getWeeklyBooking(startDate).getCaravans()) + 1.0)
						/ lastAvailableStart);
				x = fuzzyFactor * x;
			}
		}
		if (startDate.getMonthValue() >= 5 && startDate.getMonthValue() <= 8) {
			x *= 1.5;
		}
		return x;
	}

	public double calcCabinPrice(LocalDate startDate, CampsiteBookingManager campsiteBookingManager) {
		campsiteBookingManager.checkStartDay(startDate);
		double x = 0;
		if (campsiteBookingManager.getWeeklyBooking(startDate).getCabins() >= campsiteBookingManager.getCabins()) {
			x = Double.NaN;
		} else {
			x = cabinBasePrice;
		}
		if (startDate.getMonthValue() >= 5 && startDate.getMonthValue() <= 8) {
			x *= 1.5;
		}
		return x;
	}
}
