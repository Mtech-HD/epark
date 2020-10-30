package com.ePark.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ePark.dto.CarParkRegistrationDto;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;

@Service
public interface CarParkSpotService {

	CarParkSpots save(CarParkRegistrationDto carParkRegistrationDto, CarParks carPark);
	
	List<CarParkSpots> findByCarParks(CarParks carPark);
}
