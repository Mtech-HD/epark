package com.ePark.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ePark.model.Roles;
import com.ePark.repository.RoleRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RoleServiceTest {

	@Mock
	private RoleRepository roleRepo;

	@InjectMocks
	private RoleService roleService;

	@Test
	void testFindByRoleId() {
		
		Roles role = new Roles(1000L);

		Mockito.when(roleRepo.findByRoleId(1000L)).thenReturn(role);

		assertThat(roleService.findByRoleId(1000L)).isEqualTo(role);
	}

	@Test
	void testFindAll() {
		
		Roles role1 = new Roles("TestRole1");
		
		Roles role2 = new Roles("TestRole2");
		
		List<Roles> roleList = new ArrayList<>();
		roleList.add(role1);
		roleList.add(role2);

		Mockito.when(roleRepo.findAll()).thenReturn(roleList);

		assertThat(roleService.findAll()).isEqualTo(roleList);
	}

}
