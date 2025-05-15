package com.projectcode.rewardsystem.service.impl;

import com.projectcode.rewardsystem.exception.CustomerNotFoundException;
import com.projectcode.rewardsystem.model.Customer;
import com.projectcode.rewardsystem.repository.CustomerRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class RewardServiceImplTest {

    @InjectMocks
    private RewardServiceImpl rewardService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    Transaction trn1, trn2;
    LocalDate fromDate, toDate;
    Customer customer;

    @BeforeEach
    public void setup() {
        trn1 = new Transaction(1L, 1L, 120.0, LocalDate.now().minusDays(10));
        trn2 = new Transaction(2L, 1L, 75.0, LocalDate.now().minusMonths(2));

        customer = new Customer(1L, "ram");

        fromDate = LocalDate.of(2025, 3, 1);
        toDate = LocalDate.of(2025, 5, 15);
    }

    @Test
    public void testCalculateRewards_WithMultipleTransactions_returns200() {
        List<Transaction> transactions = List.of(
                new Transaction(1L, customer.getId(), 120.0, LocalDate.of(2025, 3, 15)),
                new Transaction(2L, customer.getId(), 75.0, LocalDate.of(2025, 4, 10)),
                new Transaction(3L, customer.getId(), 45.0, LocalDate.of(2025, 5, 5))
        );

        when(transactionRepository.findByCustomerIdAndDateBetween(customer.getId(), fromDate, toDate))
                .thenReturn(transactions);
        when(customerRepository.findById(customer.getId()))
                .thenReturn(Optional.of(new Customer(customer.getId(), customer.getName())));

        var response = rewardService.calculateRewards(customer.getId(), fromDate, toDate);

        assertNotNull(response);
        assertEquals(115, response.getTotalRewards());
        assertEquals(customer.getName(), response.getCustomerName());
        assertEquals(90, response.getMonthlyRewards().get("MARCH-2025"));
        assertEquals(25, response.getMonthlyRewards().get("APRIL-2025"));
        assertEquals(0,response.getMonthlyRewards().get("MAY-2025"));
    }

    @Test
    public void testCalculateRewards_NoTransactions_Returns200() {
        when(transactionRepository.findByCustomerIdAndDateBetween(customer.getId(), fromDate, toDate))
                .thenReturn(Collections.emptyList());
        when(customerRepository.findById(customer.getId()))
                .thenReturn(Optional.of(new Customer(customer.getId(), customer.getName())));

        var response = rewardService.calculateRewards(customer.getId(), fromDate, toDate);

        assertNotNull(response);
        assertEquals(0, response.getTotalRewards());
        assertEquals(customer.getName(), response.getCustomerName());
        assertTrue(response.getMonthlyRewards().isEmpty());
        assertTrue(response.getTransactions().isEmpty());
    }

    @Test
    public void testCalculateRewards_CustomerNotFound_Returns404(){
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        CustomerNotFoundException ex =
                assertThrows(CustomerNotFoundException.class,() -> rewardService.calculateRewards(customer.getId(), fromDate, toDate));
        assertEquals("Customer not found with ID: "+customer.getId(), ex.getMessage());
    }

    /* Parameterized inputs for the calculatePoints method.
       Validates the points for different amounts
       Ex: "1,0" -> '1' is the amount and 0 is the actual points needs to provide for $1 amount
       Ex: "125, 100" -> '125' is the amount and 0 is the actual points needs to provide for $100 amount
     */
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



