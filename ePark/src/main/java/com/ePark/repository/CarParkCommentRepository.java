package com.ePark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ePark.model.CarParkComments;

@Repository
public interface CarParkCommentRepository extends JpaRepository<CarParkComments, Long> {

}
