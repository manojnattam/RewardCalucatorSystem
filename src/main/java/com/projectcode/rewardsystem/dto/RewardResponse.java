package com.projectcode.rewardsystem.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RewardResponse {
    private Long customerId;
    private String customerName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Map<String, Integer> monthlyRewards;
    private int totalRewards;
    private List<TransactionDTO> transactions;
}
