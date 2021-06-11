

package com.parkit.parkingsystem.service;



import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket,boolean discount) {
		
		if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
			throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
		}

		long inHour = ticket.getInTime().getTime();
		long outHour = ticket.getOutTime().getTime();

		long duration = outHour - inHour; 
		
		switch (ticket.getParkingSpot().getParkingType()){
			case CAR: {
				
				if(duration <= 1800000) { 
					ticket.setPrice(0);
				} else {
					ticket.setPrice((duration/3600000) * Fare.CAR_RATE_PER_HOUR);
				}
				break;
			}
			case BIKE: {
				
				if (duration <= 1800000) {
					ticket.setPrice(0);
				} else {
					ticket.setPrice((duration/3600000) * Fare.BIKE_RATE_PER_HOUR);
				}
				break;
			}
			default: throw new IllegalArgumentException("Unkown Parking Type");
		}
		
		if (discount) {  
			double pr = ticket.getPrice();
			ticket.setPrice(pr * 0.95);
			
		}
	} 
} 		