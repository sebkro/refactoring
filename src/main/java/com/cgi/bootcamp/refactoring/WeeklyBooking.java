package com.cgi.bootcamp.refactoring;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class WeeklyBooking {
	
	private int cabins = 0;
	private int caravans = 0;
	private int tents = 0; 
	
	public void addTent() {
		tents += 1;
	}

	public void addCaravan() {
		caravans += 1;
	}

	public void addCabin() {
		cabins += 1;
	}

}
