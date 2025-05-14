package com.projectcode.rewardsystem.dto;

import com.projectcode.rewardsystem.model.Transaction;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardResponse {
    private Long customerId;
    private String customerName;
    private Map<String, Integer> monthlyRewards;
    private int totalRewards;
    private List<Transaction> transactions;
}
