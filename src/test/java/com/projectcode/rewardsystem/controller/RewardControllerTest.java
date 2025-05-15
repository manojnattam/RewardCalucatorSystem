package com.projectcode.rewardsystem.controller;

import com.projectcode.rewardsystem.dto.RewardResponse;
import com.projectcode.rewardsystem.exception.CustomerNotFoundException;
import com.projectcode.rewardsystem.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
public class RewardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RewardService rewardService;

    Long customerId;
    LocalDate fromDate;
    LocalDate toDate;

    @BeforeEach
    void setUp(){
        customerId = 1L;
        fromDate = LocalDate.of(2025, 03, 15);
        toDate = LocalDate.of(2025, 05, 14);
    }

    @Test
    public void testGetRewards_ReturnsValidResponse() throws Exception{
        Map<String, Integer> monthlyRewardsList = new HashMap<>();
        monthlyRewardsList.put("MARCH-2025", 50);
        monthlyRewardsList.put("APRIL-2025", 70);

        RewardResponse rewardResponse = RewardResponse.builder()
                .customerId(customerId)
                .customerName("Sam")
                .totalRewards(120)
                .monthlyRewards(monthlyRewardsList)
                .build();

        when(rewardService.calculateRewards(customerId, fromDate, toDate)).thenReturn(rewardResponse);

        mockMvc.perform(get("/api/rewards/{customerId}",customerId)
                .param("fromDate", fromDate.toString())
                .param("toDate", toDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.customerName").value("Sam"))
                .andExpect(jsonPath("$.totalRewards").value(120))
                .andExpect(jsonPath("$.monthlyRewards").value(monthlyRewardsList));

    }

    @Test
    public void testGetRewards_CustomerNotFound_Returns404() throws Exception {
        when(rewardService.calculateRewards(eq(customerId), any(), any()))
                .thenThrow(new CustomerNotFoundException("Customer not found with ID: "+ customerId));

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found with ID: "+ customerId));
    }

    @Test
    public void testGetRewards_InvalidParameterRequest_Returns400() throws Exception {

        // Swapping the FROM and TO dates in the method arguments to make date validation error
        when(rewardService.calculateRewards(customerId, toDate, fromDate))
                .thenThrow(new CustomerNotFoundException("fromDate cannot be after toDate. Please provide valid dates"));

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .param("toDate", fromDate.toString())
                        .param("fromDate", toDate.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("fromDate cannot be after toDate. Please provide valid dates"));
    }

}
