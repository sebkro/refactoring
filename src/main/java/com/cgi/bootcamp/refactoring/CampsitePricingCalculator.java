package com.cgi.bootcamp.refactoring;

import java.time.LocalDate;
import java.util.function.Supplier;

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
		int bookedTents = campsiteBookingManager.getBookedTents(startDate);
		int availableTents = campsiteBookingManager.getTents() - bookedTents;
		
		return withAvailabilityCheck(availableTents, () -> {
			double price = tentBasePrice;
			price = addLastAvailableFactor(price, availableTents);
			return addPeakSeasonPricing(startDate, price);
		});
	}
	
	public double calcCaravanPrice(LocalDate startDate, CampsiteBookingManager campsiteBookingManager) {
		campsiteBookingManager.checkStartDay(startDate);
		int bookedCaravans = campsiteBookingManager.getBookedCaravans(startDate);
		int availableCaravans = campsiteBookingManager.getCaravans() - bookedCaravans;
		
		return withAvailabilityCheck(availableCaravans, () -> {
			double price = caravanBasePrice;
			price = addLastAvailableFactor(price, availableCaravans);
			return addPeakSeasonPricing(startDate, price);
		});
	}

	public double calcCabinPrice(LocalDate startDate, CampsiteBookingManager campsiteBookingManager) {
		campsiteBookingManager.checkStartDay(startDate);
		int bookedCabins = campsiteBookingManager.getBookedCabins(startDate);
		int availableCabins = campsiteBookingManager.getCabins() - bookedCabins;
		
		return withAvailabilityCheck(availableCabins, () -> {
			double price = cabinBasePrice;
			return addPeakSeasonPricing(startDate, price);
		});
	}
	
	private double withAvailabilityCheck(int available, Supplier<Double> priceCalculation) {
		if (available > 0) {
			return priceCalculation.get();
		} else {
			return Double.NaN;
		}
	}

	private double addLastAvailableFactor(double price, int available) {
		if (available <= lastAvailableStart) {
			double factor = 1 + ((lastAvailableStart - available + 1.0) / lastAvailableStart);
			price = factor * price;
		}
		return price;
	}
	
	private double addPeakSeasonPricing(LocalDate startDate, double price) {
		if (startDate.getMonthValue() >= 5 && startDate.getMonthValue() <= 8) {
			price *= 1.5;
		}
		return price;
	}

}
