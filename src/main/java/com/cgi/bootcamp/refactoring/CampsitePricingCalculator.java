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
		double price = 0;
		if (campsiteBookingManager.getWeeklyBooking(startDate).getTents() >= campsiteBookingManager.getTents()) {
			price = Double.NaN;
		} else {
			price = tentBasePrice;
			if (campsiteBookingManager.getTents()
					- campsiteBookingManager.getWeeklyBooking(startDate).getTents() <= lastAvailableStart) {
				double factor = 1 + ((lastAvailableStart - (campsiteBookingManager.getTents()
						- campsiteBookingManager.getBookings().get(startDate).getTents()) + 1.0) / lastAvailableStart);
				price = factor * price;
			}
		}
		if (startDate.getMonthValue() >= 5 && startDate.getMonthValue() <= 8) {
			price *= 1.5;
		}
		return price;
	}

	public double calcCaravanPrice(LocalDate startDate, CampsiteBookingManager campsiteBookingManager) {
		campsiteBookingManager.checkStartDay(startDate);
		double price = 0;
		if (campsiteBookingManager.getWeeklyBooking(startDate).getCaravans() >= campsiteBookingManager.getCaravans()) {
			price = Double.NaN;
		} else {
			price = caravanBasePrice;
			if (campsiteBookingManager.getCaravans()
					- campsiteBookingManager.getWeeklyBooking(startDate).getCaravans() <= lastAvailableStart) {
				double fuzzyFactor = 1 + ((lastAvailableStart - (campsiteBookingManager.getCaravans()
						- campsiteBookingManager.getWeeklyBooking(startDate).getCaravans()) + 1.0)
						/ lastAvailableStart);
				price = fuzzyFactor * price;
			}
		}
		if (startDate.getMonthValue() >= 5 && startDate.getMonthValue() <= 8) {
			price *= 1.5;
		}
		return price;
	}

	public double calcCabinPrice(LocalDate startDate, CampsiteBookingManager campsiteBookingManager) {
		campsiteBookingManager.checkStartDay(startDate);
		double price = 0;
		if (campsiteBookingManager.getWeeklyBooking(startDate).getCabins() >= campsiteBookingManager.getCabins()) {
			price = Double.NaN;
		} else {
			price = cabinBasePrice;
		}
		if (startDate.getMonthValue() >= 5 && startDate.getMonthValue() <= 8) {
			price *= 1.5;
		}
		return price;
	}
}
