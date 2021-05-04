package com.ePark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ePark.model.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long>{

	public Roles findByName(String name);
	
	Roles findByRoleId(long roleId);
	
}