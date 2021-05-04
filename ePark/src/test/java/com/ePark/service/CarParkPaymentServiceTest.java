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

import com.ePark.model.CarParkPayments;
import com.ePark.model.CarParks;
import com.ePark.repository.CarParkPaymentRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CarParkPaymentServiceTest {

	@Mock
	private CarParkPaymentRepository carParkPaymentRepo;

	@InjectMocks
	private CarParkPaymentService carParkPaymentService;
	
	@Test
	void testSave() {
		CarParkPayments carParkPayment = new CarParkPayments(1000L);

		Mockito.when(carParkPaymentRepo.save(carParkPayment)).thenReturn(carParkPayment);

		assertThat(carParkPaymentService.save(carParkPayment)).isEqualTo(carParkPayment);
	}

	/*
	 * @Test void testFindByCarParksAndyearMonth() { }
	 */

	@Test
	void testFindByCarParksAndPaid() {
		
		CarParkPayments carParkPayment = new CarParkPayments(1000L);
		
		CarParks carPark = new CarParks(1000L);
		carParkPayment.setCarParks(carPark);

		carParkPayment.setPaid(false);
		
		List<CarParkPayments> carParkPaymentsList = new ArrayList<>();
		carParkPaymentsList.add(carParkPayment);
		
		Mockito.when(carParkPaymentRepo.findByCarParksAndPaid(carPark, false)).thenReturn(carParkPaymentsList);
	}

	@Test
	void testSaveIfNotExists() {

		CarParkPayments carParkPayment = new CarParkPayments(1000L);
		
		CarParks carPark = new CarParks(1000L);
		carParkPayment.setCarParks(carPark);
		carParkPayment.setYearMonth("202101");
		
		Mockito.when(carParkPaymentRepo.findByCarParksAndYearMonth(carParkPayment.getCarParks(), carParkPayment.getYearMonth())).thenReturn(null);

		Mockito.when(carParkPaymentRepo.save(carParkPayment)).thenReturn(carParkPayment);

		assertThat(carParkPaymentService.save(carParkPayment)).isEqualTo(carParkPayment);
		
	}

}
