package com.ePark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.model.Users;
import com.ePark.model.Vehicles;
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

	public void removeVehicleFromUser(long vehicleId, Users user) {

		Vehicles vehicle = vehicleRepo.findByVehicleId(vehicleId);

		if (vehicle.getIsDefault()) {
			List<Vehicles> vehicles = vehicleRepo.findByUsers(user);

			setDefault(vehicleId, false);

			if (vehicles != null) {
				setDefault(vehicles.get(0).getVehicleId(), true);
			}
		}

		vehicle.setUsers(null);
		vehicleRepo.save(vehicle);
	}

	public Vehicles setDefault(long vehicleId, boolean isDefault) {

		Vehicles vehicle = vehicleRepo.findByVehicleId(vehicleId);

		vehicle.setIsDefault(isDefault);

		return vehicleRepo.save(vehicle);
	}

	public Vehicles findByVehicleId(long vehicleId) {
		return vehicleRepo.findByVehicleId(vehicleId);
	}

	public Vehicles findByUsersAndIsDefault(long userId) {
		return vehicleRepo.findByUsersAndIsDefault(userId, true);
	}
}
