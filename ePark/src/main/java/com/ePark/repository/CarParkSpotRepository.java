package com.ePark.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ePark.entity.CarParkSpots;
import com.ePark.entity.CarParks;

@Repository
public interface CarParkSpotRepository extends JpaRepository<CarParkSpots, Long> {

	void save(Set<CarParkSpots> carParkSpots);
	
	List<CarParkSpots> findByCarParks(CarParks carPark);
	
	CarParkSpots findByCarParkSpotId(long carParkSpotId);
	
	
	@Query(value = "CALL checkAvailable(:carParkId, :bookingDate, :startTime, :endTime, :isDisabled, :length)", nativeQuery = true)
	CarParkSpots checkAvailable(@Param("carParkId") long carParkId, 
			@Param("bookingDate") LocalDate bookingDate,
			@Param("startTime") LocalTime startTime,
			@Param("endTime") LocalTime endTime,
			@Param("isDisabled") boolean isDisabled,
			@Param("length") int length);
	
	@Query(value = "CALL checkAvailable(:carParkId, :bookingDate, :startTime, :endTime, :isDisabled, :length)", nativeQuery = true)
	List<CarParkSpots> getMultipleFreeSpaces(@Param("carParkId") long carParkId, 
			@Param("bookingDate") LocalDate bookingDate,
			@Param("startTime") LocalTime startTime,
			@Param("endTime") LocalTime endTime,
			@Param("isDisabled") boolean isDisabled,
			@Param("length") int length);
	
}