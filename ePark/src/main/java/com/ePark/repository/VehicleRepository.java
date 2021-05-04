package com.ePark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ePark.model.Users;
import com.ePark.model.Vehicles;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicles, Long>{

	List<Vehicles> findByUsers(Users user);
	
	/*
	 * @Modifying
	 * 
	 * @Query(value =
	 * "UPDATE vehicles SET userId = null WHERE vehicleId = :vehicleId ",
	 * nativeQuery = true) void removeVehicleFromUser(long vehicleId);
	 */
	
	Vehicles findByVehicleId(long vehicleId);
	
	@Query(value = "SELECT * FROM vehicles WHERE userId = :userId and isDefault = :isDefault LIMIT 1", nativeQuery = true)
	Vehicles findByUsersAndIsDefault(long userId, boolean isDefault);
}
