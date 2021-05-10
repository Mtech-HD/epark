package com.ePark.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.ePark.model.Week;

@Component
public class Utils {

	public String getDayOfWeek(LocalDate date) {

	    return DayOfWeek.from(date).toString();
	}
	
	public Week getWeekEnum(String weekDay) {
		switch (weekDay.toUpperCase()) {
		case "MONDAY": 
			return Week.MONDAY;
		case "TUESDAY": 
			return Week.TUESDAY;
		case "WEDNESDAY": 
			return Week.WEDNESDAY;
		case "THURSDAY": 
			return Week.THURSDAY;
		case "FRIDAY": 
			return Week.FRIDAY;
		case "SATURDAY": 
			return Week.SATURDAY;
		case "SUNDAY": 
			return Week.SUNDAY;
		}
		return Week.MONDAY;
	}
	
}
