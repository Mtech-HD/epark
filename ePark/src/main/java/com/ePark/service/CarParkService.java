package com.ePark.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ePark.dto.CarParkDto;
import com.ePark.model.CarParkStatus;
import com.ePark.model.CarParks;
import com.ePark.model.Users;

@Service
public interface CarParkService {
	
	CarParks save(CarParks carPark);

	void save(CarParkDto carParkRegistrationDto);

	List<CarParks> findAll();
	
	CarParks findByCarParkId(long carParkId);
	
	List<CarParks> findByCarParkStatus(CarParkStatus status);
	
	List<CarParks> findByUsers(Users user);
}


