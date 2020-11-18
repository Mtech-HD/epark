package com.ePark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ePark.model.Vehicles;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicles, Long>{

}
