package com.ePark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.model.CarParkComments;
import com.ePark.repository.CarParkCommentRepository;

@Service
public class CarParkCommentServiceImpl implements CarParkCommentService {

	@Autowired
	private CarParkCommentRepository carParkCommentRepo;
	
	@Override
	public CarParkComments save(CarParkComments carParkComments) {

		return carParkCommentRepo.save(carParkComments);
	}

}
