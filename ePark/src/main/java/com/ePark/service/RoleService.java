package com.ePark.service;

import org.springframework.stereotype.Service;

import com.ePark.model.Roles;

@Service
public interface RoleService {

	Roles save(Roles role);
	
	Roles findByName(String name);
	
	Iterable<Roles> findAll();

}