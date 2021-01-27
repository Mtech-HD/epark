package com.ePark.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.ePark.entity.Bookings;
import com.stripe.model.PaymentMethodCollection;

public class BookingFlow {
	
    public enum Step {
        Dates(0),
        Vehicle(1),
        Review(2),
        Payment(3);

        int flowStep;

        Step(int flowStep) {
            this.flowStep = flowStep;
        }

        public static Step from(int flowStep) {
            switch (flowStep) {
                case 0:
                    return Dates;
                case 1:
                    return Vehicle;
                case 2:
                    return Review;
                case 3:
                    return Payment;
                default:
                    return Dates;
            }
        }
    }
    
    @Valid
    private Bookings booking = new Bookings();

    private List<StepDescription> stepDescriptions = new ArrayList<>();

    private Set<Step> completedSteps = new HashSet<>();

    private Step activeStep = Step.Dates;

    public BookingFlow() {
        stepDescriptions.add(new StepDescription(0, "Dates", "Choose your booking dates"));
        stepDescriptions.add(new StepDescription(1, "Vehicle", "Choose your vehicle"));
        stepDescriptions.add(new StepDescription(2, "Review", "Verify your booking"));
        stepDescriptions.add(new StepDescription(3, "Payment", "Provide payment details"));
    }

    public Bookings getBooking() {
        return booking;
    }

    public void setBooking(Bookings booking) {
        this.booking = booking;
    }

    public void setActive(Step step) {
        activeStep = step;
    }

    public Step getActiveStep() {
        return activeStep;
    }

    public StepDescription getActiveStepDescription() {
        return stepDescriptions.get(activeStep.flowStep);
    }

    public void completeStep(Step step) {
        completedSteps.add(step);
    }

    public void incompleteStep(Step step) {
        completedSteps.remove(step);
    }

    public boolean isActive(Step step) {
        return step == activeStep;
    }

    public boolean isCompleted(Step step) {
        return completedSteps.contains(step);
    }

    public void enterStep(Step step) {
        setActive(step);
        incompleteStep(step);
    }

    public List<StepDescription> getStepDescriptions() {
        return stepDescriptions;
    }

    public static class StepDescription {
        private int flowStep;
        private String title;
        private String description;

        public StepDescription(int flowStep, String title, String description) {
            this.flowStep = flowStep;
            this.title = title;
            this.description = description;
        }

        public int getFlowStep() {
            return flowStep;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        private int normalizedFlowStep() {
            return flowStep + 1;
        }

        public String getFlowStepWithTitle() {
            return normalizedFlowStep() + ". " + title;
        }

        public String getFlowStepWithDescription() {
            return normalizedFlowStep() + ". " + description;
        }

        @Override
        public String toString() {
            return "StepDescription{" +
                    "flowStep=" + flowStep +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
	
}
