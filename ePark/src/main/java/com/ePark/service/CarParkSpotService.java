package com.ePark.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ePark.dto.CarParkDto;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;

@Service
public interface CarParkSpotService {

	
	List<CarParkSpots> findByCarParks(CarParks carPark);
}
