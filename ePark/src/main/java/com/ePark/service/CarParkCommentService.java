package com.ePark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.entity.CarParkComments;
import com.ePark.repository.CarParkCommentRepository;

@Service
public class CarParkCommentService {

	@Autowired
	private CarParkCommentRepository carParkCommentRepo;

	public CarParkComments save(CarParkComments carParkComments) {

		return carParkCommentRepo.save(carParkComments);
	}

}
