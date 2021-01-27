package com.ePark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ePark.entity.Users;
import com.ePark.entity.Vehicles;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicles, Long>{

	List<Vehicles> findByUsers(Users user);
	
	void deleteByVehicleId(long vehicleId);
}
