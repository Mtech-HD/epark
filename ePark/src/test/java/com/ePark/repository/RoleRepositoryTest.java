package com.ePark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ePark.model.Roles;

@DataJpaTest
class RoleRepositoryTest {

	@Autowired
	private RoleRepository roleRepo;

	@Test
	void testFindByName() {

		Roles role = new Roles("Test Role");
		
		roleRepo.save(role);
		
		assertThat(roleRepo.findByName(role.getName())).isEqualTo(role);
	}

	@Test
	void testFindByRoleId() {
		
		Roles role = new Roles("Test Role");
		
		roleRepo.save(role);
		
		assertThat(roleRepo.findByRoleId(role.getRoleId())).isEqualTo(role);
	}

}
