package com.ePark.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ePark.dto.CarParkRegistrationDto;
import com.ePark.model.CarParkStatus;
import com.ePark.model.CarParks;

@Service
public interface CarParkService {

	void save(CarParkRegistrationDto carParkRegistrationDto);

	List<CarParks> findAll();
	
	CarParks findByCarParkId(long carParkId);
	
	List<CarParks> findByCarParkStatus(CarParkStatus status);
}


