package com.ePark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ePark.model.CarParkStatus;
import com.ePark.model.CarParks;


@Repository
public interface CarParkRepository extends JpaRepository<CarParks, Long>{
	
	List<CarParks> findAll();
	
	CarParks findByCarParkId(long carParkId);
	
	List<CarParks> findByCarParkStatus(CarParkStatus status);
}
