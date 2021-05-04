package com.ePark.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ePark.service.BookingFlow;
import com.ePark.service.BookingService;

import ch.qos.logback.classic.Logger;

@Controller
public class ErrorsController implements ErrorController {

	@Autowired
	private BookingService bookingService;

	private static final String PATH = "/error";

	@RequestMapping(value = PATH)
	@Transactional
	public String handleError(HttpServletRequest httpRequest, Model model) {
		/*
		 * Object status =
		 * httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE); Integer
		 * statusCode = Integer.valueOf(status.toString());
		 * 
		 * if(statusCode == HttpStatus.NOT_FOUND.value()) { Logger logger = (Logger)
		 * LoggerFactory.getLogger(ErrorsController.class);
		 * logger.error("An exception occurred!"); }
		 */

		HttpSession session = httpRequest.getSession();
		BookingFlow bookingFlow = (BookingFlow) session.getAttribute("bookingFlow");

		if (bookingFlow != null) {
			if (bookingFlow.getBooking().getBookingId() != 0) {
				bookingService.delete(bookingFlow.getBooking().getBookingId());
			}

		}

		
		
	    Object status = httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    String message = null;
	    
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	    
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	        	
	        	message = "Page not found";
	        	
	        } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	        	
	            message = "Internal server error";
	            
	        } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
	        	
	            message = "You do not have permission to access this page";
	        }
	    }
		

		model.addAttribute("message", message);

		return "error";
	}

	@Override
	public String getErrorPath() {

		return PATH;
	}
}
