package com.projectcode.rewardsystem.service;

import com.projectcode.rewardsystem.model.Customer;
import com.projectcode.rewardsystem.repository.CustomerRepository;
import com.projectcode.rewardsystem.service.RewardService;
import com.projectcode.rewardsystem.model.Transaction;
import com.projectcode.rewardsystem.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class RewardServiceTest {

    @InjectMocks
    private RewardService rewardService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    Transaction trn1, trn2;
    Customer customer1;

    @BeforeEach
    public void setup() {
        trn1 = new Transaction(1L, 1L, 120.0, LocalDate.now().minusDays(10));
        trn2 = new Transaction(2L, 1L, 75.0, LocalDate.now().minusMonths(2));

        customer1 = new Customer(1L, "ram");
    }

    @Test
    public void testRewardCalculation() {
        LocalDate from = LocalDate.now().minusMonths(3);
        LocalDate to = LocalDate.now();

        when(transactionRepository.findByCustomerIdAndDateBetween(customer1.getId(), from, to))
                .thenReturn(Arrays.asList(trn1, trn2));

        var response = rewardService.calculateRewards(customer1.getId(), from, to);
        assertEquals(90 + 25, response.getTotalRewards());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "1, 0",
            "20, 0",
            "50, 0",
            "51, 1",
            "70, 20",
            "100, 50",
            "101, 52",
            "125, 100"
    })
    public void testCalculatePoints(double amount, int actualPoints){
            int points = rewardService.calculatePoints(amount);

            assertEquals(points, actualPoints);

    }
}
