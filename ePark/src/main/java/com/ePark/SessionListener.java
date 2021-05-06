package com.ePark;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ePark.service.BookingFlow;
import com.ePark.service.BookingService;

@Component
public class SessionListener implements HttpSessionListener {

	@Autowired
	private BookingService bookingService;

	@Override
	@Transactional
	public void sessionDestroyed(HttpSessionEvent event) {

		HttpSession session = event.getSession();
		BookingFlow bookingFlow = (BookingFlow) session.getAttribute("bookingFlow");

		if (bookingFlow != null) {
			bookingService.delete(bookingFlow.getBooking().getBookingId());
		}

	}

}
