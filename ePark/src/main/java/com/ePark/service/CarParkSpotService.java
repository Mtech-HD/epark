package com.ePark.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.entity.CarParkSpots;
import com.ePark.entity.CarParks;
import com.ePark.repository.CarParkSpotRepository;

@Service
public class CarParkSpotService {

	@Autowired
	private CarParkSpotRepository carParkSpotRepo;
	
	public List<CarParkSpots> findByCarParks(CarParks carPark) {
		
		return carParkSpotRepo.findByCarParks(carPark);
	}
	
	public CarParkSpots findByCarParkSpotId(long carParkSpotId) {
		return carParkSpotRepo.findByCarParkSpotId(carParkSpotId);
	}
	
	public CarParkSpots checkAvailable(long carParkId, LocalDate bookingDate, LocalTime startTime, LocalTime endTime, boolean isDisabled) {
		
		return carParkSpotRepo.checkAvailable(carParkId, bookingDate, startTime, endTime, isDisabled);
	}

}
