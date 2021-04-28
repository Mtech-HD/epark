package com.ePark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ePark.entity.CarParkTimes;
import com.ePark.entity.CarParks;


@Repository
public interface CarParkTimeRepository extends JpaRepository<CarParkTimes, Long>{
	
	
	List<CarParkTimes> findByCarParks(CarParks carPark);
	
	
	@Query(value = "CALL getCarParkTimes(:carParkId);", nativeQuery = true)
	List<CarParkTimes> getCarParkTimes(@Param("carParkId") long carParkId);
	
	CarParkTimes findByCarParkTimeId(long carParkTimeId); 
}
