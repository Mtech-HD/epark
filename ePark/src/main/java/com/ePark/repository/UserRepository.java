package com.ePark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ePark.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

	Users findByUsername(String username);

	Users findByUserId(long userId);

	Users findByStripeId(String stripeId);

	Users findByEmail(String email);

	Users findByResetPasswordToken(String token);
}
