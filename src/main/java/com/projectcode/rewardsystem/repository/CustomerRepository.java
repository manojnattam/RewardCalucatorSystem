package com.projectcode.rewardsystem.repository;

import com.projectcode.rewardsystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}