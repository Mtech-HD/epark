package com.ePark.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;

@Repository
public interface CarParkSpotRepository extends JpaRepository<CarParkSpots, Long> {

	void save(Set<CarParkSpots> carParkSpots);
	
	List<CarParkSpots> findByCarParks(CarParks carPark);
}