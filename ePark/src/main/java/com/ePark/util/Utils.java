package com.ePark.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.ePark.entity.Week;

@Component
public class Utils {

	public String getDayOfWeek(LocalDate date) {

	    return DayOfWeek.from(date).toString();
	}
	
	public Week getWeekEnum(String weekDay) {
		switch (weekDay) {
		case "Monday": 
			return Week.MONDAY;
		case "Tuesday": 
			return Week.TUESDAY;
		case "Wednesday": 
			return Week.WEDNESDAY;
		case "Thursday": 
			return Week.THURSDAY;
		case "Friday": 
			return Week.FRIDAY;
		case "Saturday": 
			return Week.SATURDAY;
		case "Sunday": 
			return Week.SUNDAY;
		}
		return Week.MONDAY;
	}
	
}
