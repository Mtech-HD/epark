package com.ePark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ePark.model.CarParks;
import com.ePark.model.Users;
import com.ePark.model.CarParks.CarParkStatus;

@Repository
public interface CarParkRepository extends JpaRepository<CarParks, Long> {

	List<CarParks> findAll();

	CarParks findByCarParkId(long carParkId);

	List<CarParks> findByCarParkStatus(CarParkStatus status);

	List<CarParks> findByUsers(Users user);

	List<CarParks> findByDynamicPricing(boolean dynamicPricing);
}
