package com.projectcode.rewardsystem.service.impl;

import com.projectcode.rewardsystem.dto.RewardResponse;
import com.projectcode.rewardsystem.dto.TransactionDTO;
import com.projectcode.rewardsystem.exception.CustomerNotFoundException;
import com.projectcode.rewardsystem.utility.DateValidator;
import com.projectcode.rewardsystem.utility.TransactionMapper;
import com.projectcode.rewardsystem.model.Customer;
import com.projectcode.rewardsystem.model.Transaction;
import com.projectcode.rewardsystem.repository.CustomerRepository;
import com.projectcode.rewardsystem.repository.TransactionRepository;
import com.projectcode.rewardsystem.service.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RewardServiceImpl implements RewardService {
    private static final Logger log = LoggerFactory.getLogger(RewardServiceImpl.class);

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public RewardResponse calculateRewards(Long customerId, LocalDate fromDate, LocalDate toDate) {
        log.info("Validating date ranges from: {} to: {}", fromDate, toDate);
        DateValidator.validateDateRange(fromDate, toDate);

        log.info("Normalizing date inputs if any are null");
        DateValidator.DateRange validatedDates = DateValidator.normalizeDateInputs(fromDate, toDate);
        fromDate = validatedDates.fromDate();
        toDate = validatedDates.toDate();
        log.info("Normalized dates fromDate: {} toDates:{}", fromDate, toDate);

        List<Transaction> transactions = transactionRepository.findByCustomerIdAndDateBetween(customerId, fromDate, toDate);
        String customerName = customerRepository.findById(customerId)
                .map(Customer::getName)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: "+ customerId));

        Map<String, Integer> monthlyRewards = new HashMap<>();
        int total = 0;

        for (Transaction tx : transactions) {
            int points = calculatePoints(tx.getAmount());
            String month = tx.getDate().getMonth().toString() + "-" + tx.getDate().getYear();
            log.info(month+" month points: "+ points + "for the amount: "+ tx.getAmount());
            monthlyRewards.put(month, monthlyRewards.getOrDefault(month, 0) + points);
            total += points;
        }
        log.info("Total points: "+total);
        List<TransactionDTO> transactionDTOList = TransactionMapper.toDTOList(transactions);

        return RewardResponse.builder()
                .customerId(customerId)
                .customerName(customerName)
                .fromDate(fromDate)
                .toDate(toDate)
                .monthlyRewards(monthlyRewards)
                .totalRewards(total)
                .transactions(transactionDTOList)
                .build();
    }

    public int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int) ((amount - 100) * 2 + 50);
        } else if (amount > 50) {
            points += (int) (amount - 50);
        }
        return points;
    }
}
