package com.ePark.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.model.Roles;
import com.ePark.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepo;

	
	@Override
	public Roles save(Roles role) {
		Roles userRole = new Roles(role.getName());
		return roleRepo.save(userRole);
	}
	
	@Override
	public Roles findByName(String name) {
		return roleRepo.findByName(name);
	}
	
	@Override
	public Iterable<Roles> findAll() {
		
		return roleRepo.findAll();
	}

}
