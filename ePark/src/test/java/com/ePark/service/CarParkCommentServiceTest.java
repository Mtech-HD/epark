package com.ePark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ePark.dto.CarParkDto;
import com.ePark.model.Bookings;
import com.ePark.model.CarParkComments;
import com.ePark.repository.BookingRepository;
import com.ePark.repository.CarParkCommentRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CarParkCommentServiceTest {
	
	@Mock
	private CarParkCommentRepository carParkCommentRepo;

	@InjectMocks
	private CarParkCommentService carParkCommentService;

	@Test
	void testSave() {
		CarParkComments carParkComment = new CarParkComments(1000L);

		Mockito.when(carParkCommentRepo.save(carParkComment)).thenReturn(carParkComment);

		assertThat(carParkCommentService.save(carParkComment)).isEqualTo(carParkComment);
	}

}
