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
		int bookedTents = campsiteBookingManager.getBookedTents(startDate);

		if (bookedTents >= campsiteBookingManager.getTents()) {
			price = Double.NaN;
		} else {
			price = tentBasePrice;
			int availableTents = campsiteBookingManager.getTents() - bookedTents;
			if (availableTents <= lastAvailableStart) {
				double factor = 1 + ((lastAvailableStart - availableTents + 1.0) / lastAvailableStart);
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
		int bookedCaravans = campsiteBookingManager.getBookedCaravans(startDate);
		if (bookedCaravans >= campsiteBookingManager.getCaravans()) {
			price = Double.NaN;
		} else {
			price = caravanBasePrice;
			int availableCaravans = campsiteBookingManager.getCaravans() - bookedCaravans;
			if (availableCaravans <= lastAvailableStart) {
				double fuzzyFactor = 1 + ((lastAvailableStart - availableCaravans + 1.0)
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
		int bookedCabins = campsiteBookingManager.getBookedCabins(startDate);
		if (bookedCabins >= campsiteBookingManager.getCabins()) {
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
