package com.ePark.service;

import org.springframework.stereotype.Service;

import com.ePark.model.Vehicles;

@Service
public interface VehicleService {

	
	Vehicles save(Vehicles vehicle);
}
