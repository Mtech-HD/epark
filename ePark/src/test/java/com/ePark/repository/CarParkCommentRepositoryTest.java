package com.ePark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.ePark.model.CarParkComments;
import com.ePark.model.CarParks;
import com.ePark.model.Users;

@DataJpaTest
class CarParkCommentRepositoryTest {
	
	@Autowired
	private CarParkCommentRepository carParkCommentRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testSave() {
		
		CarParks carPark = new CarParks();
		
		carPark = entityManager.persist(carPark);
		
		Users user = entityManager.persist(new Users());
		
		CarParkComments carParkComment = new CarParkComments();
		
		carParkComment.setCarParks(carPark);
		carParkComment.setUsers(user);
		
		carParkComment = carParkCommentRepo.save(carParkComment);
		
		assertThat(carParkComment).isNotNull();
	}

}
