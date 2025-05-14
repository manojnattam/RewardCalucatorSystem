package com.projectcode.rewardsystem.service;

import com.projectcode.rewardsystem.dto.RewardResponse;
import com.projectcode.rewardsystem.model.Customer;
import com.projectcode.rewardsystem.model.Transaction;
import com.projectcode.rewardsystem.repository.CustomerRepository;
import com.projectcode.rewardsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RewardService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private CustomerRepository customerRepository;

    public RewardResponse calculateRewards(Long customerId, LocalDate from, LocalDate to) {
        List<Transaction> transactions = transactionRepo.findByCustomerIdAndDateBetween(customerId, from, to);
        String customerName = customerRepository.findById(customerId)
                .map(Customer::getName)
                .orElse("NA");

        Map<String, Integer> monthlyRewards = new HashMap<>();
        int total = 0;

        for (Transaction tx : transactions) {
            int points = calculatePoints(tx.getAmount());
            String month = tx.getDate().getMonth().toString() + "-" + tx.getDate().getYear();
            monthlyRewards.put(month, monthlyRewards.getOrDefault(month, 0) + points);
            total += points;
        }
        return RewardResponse.builder()
                .customerId(customerId)
                .customerName(customerName)
                .monthlyRewards(monthlyRewards)
                .totalRewards(total)
                .transactions(transactions)
                .build();
        //return new RewardResponse(customerId, customerName, monthlyRewards, total, transactions);
    }

    protected int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int) ((amount - 100) * 2 + 50);
        } else if (amount > 50) {
            points += (int) (amount - 50);
        }
        return points;
    }
}
