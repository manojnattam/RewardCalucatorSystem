package com.projectcode.rewardsystem.service;

import com.projectcode.rewardsystem.dto.RewardResponse;

import java.time.LocalDate;

public interface RewardService {
    public RewardResponse calculateRewards(Long customerId, LocalDate from, LocalDate to);
    public int calculatePoints(double amount);
}
