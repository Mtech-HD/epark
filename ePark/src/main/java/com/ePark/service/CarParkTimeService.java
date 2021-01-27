package com.ePark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.entity.CarParkTimes;
import com.ePark.entity.CarParks;
import com.ePark.repository.CarParkTimeRepository;

@Service
public class CarParkTimeService {

	@Autowired
	private CarParkTimeRepository carParkTimeRepo;
	
	public List<CarParkTimes> findByCarParks(CarParks carPark) {
		
		return carParkTimeRepo.findByCarParks(carPark);
	}
	
	public List<CarParkTimes> getCarParkTimes(long carParkId) {
		
		return carParkTimeRepo.getCarParkTimes(carParkId);
	}
}
