package com.projectcode.rewardsystem.repository;

import com.projectcode.rewardsystem.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomerIdAndDateBetween(Long customerId, LocalDate from, LocalDate to);
}