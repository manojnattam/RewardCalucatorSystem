package com.projectcode.rewardsystem.controller;

import com.projectcode.rewardsystem.dto.RewardResponse;
import com.projectcode.rewardsystem.exception.CustomerNotFoundException;
import com.projectcode.rewardsystem.exception.InvalidParameterException;
import com.projectcode.rewardsystem.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        fromDate = LocalDate.of(2025, 3, 15);
        toDate = LocalDate.of(2025, 5, 14);
    }

    @Test
    @DisplayName("Should return valid reward response for a valid customer ID and date range")
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
    @DisplayName("Should return 400 Invalid Parameter exception when Date is not valid LocalDate(yyyy-mm-dd")
    public void testGetRewards_InvalidParameters_Returns400() throws Exception {

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .param("fromDate", "2025-19-05")) //yyyy-dd-mm
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .param("fromDate", "2025/10/15")) //yyyy-dd-mm
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .param("toDate", "2025-20-05")) //yyyy-dd-mm
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .param("fromDate", "05-20-2025")) //mm-dd-yyyy
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Should return 404 Customer Not Found exception when customer id does not exists")
    public void testGetRewards_CustomerNotFound_Returns404() throws Exception {

        String message = "Customer not found with ID: "+ customerId;

        when(rewardService.calculateRewards(eq(customerId), any(), any()))
                .thenThrow(new CustomerNotFoundException(message));

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(message));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when invalid parameters (fromDate is after toDate")
    public void testGetRewards_InvalidParameterFromDateIsAfterToDate_Returns400() throws Exception {

        String message = "fromDate cannot be after toDate. Please provide valid dates";

        // Swapping the FROM and TO dates in the method arguments to make date validation error
        when(rewardService.calculateRewards(customerId, toDate, fromDate))
                .thenThrow(new InvalidParameterException(message));

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .param("toDate", fromDate.toString())
                        .param("fromDate", toDate.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when invalid parameters (fromDate or toDate is future date")
    public void testGetRewards_InvalidParameterFromDateIsFutureDate_Returns400() throws Exception {

        fromDate = LocalDate.now().plusDays(1);
        String message = "Date cannot be in future. Please provide valid date";

        when(rewardService.calculateRewards(customerId, fromDate, toDate))
                .thenThrow(new InvalidParameterException("from"+message));

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("from"+message));

        toDate = LocalDate.now().plusMonths(3);

        when(rewardService.calculateRewards(customerId,fromDate, toDate))
                .thenThrow(new InvalidParameterException("to"+message));

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("to"+message));
    }


}
