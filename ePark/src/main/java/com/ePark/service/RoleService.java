package com.ePark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.entity.Roles;
import com.ePark.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepo;

	public Roles save(Roles role) {
		Roles userRole = new Roles(role.getName());
		return roleRepo.save(userRole);
	}

	public Roles findByName(String name) {
		return roleRepo.findByName(name);
	}

	public Iterable<Roles> findAll() {
		
		return roleRepo.findAll();
	}

}
