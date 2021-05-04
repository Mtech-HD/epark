package com.ePark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.model.Roles;
import com.ePark.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepo;
	
	public Roles findByRoleId(long roleId) {
		return roleRepo.findByRoleId(roleId);
	}

	public List<Roles> findAll() {
		
		return roleRepo.findAll();
	}
	
}
