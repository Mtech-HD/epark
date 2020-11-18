package com.ePark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.model.Vehicles;
import com.ePark.repository.VehicleRepository;

@Service
public class VehicleServiceImpl implements VehicleService {
	
	@Autowired
	private VehicleRepository vehicleRepo;
	
	@Override
	public Vehicles save(Vehicles vehicle) {
		return vehicleRepo.save(vehicle);
	}
}
