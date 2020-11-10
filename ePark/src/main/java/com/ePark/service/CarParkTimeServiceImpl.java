package com.ePark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.model.CarParkTimes;
import com.ePark.model.CarParks;
import com.ePark.repository.CarParkTimeRepository;

@Service
public class CarParkTimeServiceImpl implements CarParkTimeService {

	@Autowired
	private CarParkTimeRepository carParkTimeRepo;
	
	@Override
	public List<CarParkTimes> findByCarParks(CarParks carPark) {
		
		return carParkTimeRepo.findByCarParks(carPark);
	}
	
	@Override
	public List<CarParkTimes> getCarParkTimes(long carParkId) {
		
		return carParkTimeRepo.getCarParkTimes(carParkId);
	}
}
