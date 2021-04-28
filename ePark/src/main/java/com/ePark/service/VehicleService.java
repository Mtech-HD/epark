package com.ePark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.entity.Users;
import com.ePark.entity.Vehicles;
import com.ePark.repository.VehicleRepository;

@Service
public class VehicleService {
	
	@Autowired
	private VehicleRepository vehicleRepo;
	
	public List<Vehicles> findAll() {
		return vehicleRepo.findAll();
	}
	
	public Vehicles save(Vehicles vehicle) {
		return vehicleRepo.save(vehicle);
	}
	
	public List<Vehicles> findByUsers(Users user) {
		return vehicleRepo.findByUsers(user);
	}
	
	public void deleteByVehicleId(long vehicleId) {
		vehicleRepo.deleteByVehicleId(vehicleId);
	}
	
	public Vehicles findByVehicleId(long vehicleId) {
		return vehicleRepo.findByVehicleId(vehicleId);
	}
	
	public Vehicles findByUsersAndIsDefault(long userId) {
		return vehicleRepo.findByUsersAndIsDefault(userId, true);
	}
}
