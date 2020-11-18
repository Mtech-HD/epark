package com.ePark.service;

import org.springframework.stereotype.Service;

import com.ePark.model.CarParkComments;

@Service
public interface CarParkCommentService {

	CarParkComments save(CarParkComments carParkComment);
	
}
