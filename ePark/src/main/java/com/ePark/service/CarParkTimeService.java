package com.ePark.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ePark.model.CarParkTimes;
import com.ePark.model.CarParks;

@Service
public interface CarParkTimeService {
	
	
	List<CarParkTimes> findByCarParks(CarParks carPark);

	
	List<CarParkTimes> getCarParkTimes(long carParkId);
}



