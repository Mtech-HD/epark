package com.ePark.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ePark.entity.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long>{


	public Roles findByName(String name);
	
}