package com.ePark.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;

@Repository
public interface CarParkSpotRepository extends JpaRepository<CarParkSpots, Long> {

	void save(Set<CarParkSpots> carParkSpots);

	List<CarParkSpots> findByCarParks(CarParks carPark);

	CarParkSpots findByCarParkSpotId(long carParkSpotId);

	@Query(value = "CALL checkAvailable(:carParkId, :bookingDate, :startTime, :endTime, :isDisabled, :length)", nativeQuery = true)
	List<CarParkSpots> getFreeSpaces(@Param("carParkId") long carParkId, @Param("bookingDate") LocalDate bookingDate,
			@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime,
			@Param("isDisabled") boolean isDisabled, @Param("length") int length);

	@Query(value = "SELECT MAX(spaceNumber) FROM carParkSpots where carParkId = :carParkId", nativeQuery = true)
	public int findLargestSpaceNumber(long carParkId);

	CarParkSpots findByCarParksAndSpaceNumber(CarParks carPark, int highestSpaceNumber);

	@Modifying
	@Query(value = "DELETE FROM carParkSpots where carParkSpotId = :carParkSpotId", nativeQuery = true)
	void deleteByCarParkSpotId(long carParkSpotId);

	List<CarParkSpots> findByCarParksAndIsDisabledOrderBySpaceNumberAsc(CarParks carPark, boolean isDisabled);
}