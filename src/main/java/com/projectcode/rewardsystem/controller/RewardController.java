package com.projectcode.rewardsystem.controller;

import com.projectcode.rewardsystem.dto.RewardResponse;
import com.projectcode.rewardsystem.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @GetMapping("/{customerId}")
    public ResponseEntity<RewardResponse> getRewards(
            @PathVariable Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        if (from == null || to == null) {
            to = LocalDate.now();
            from = to.minusMonths(3);
        }
        RewardResponse response = rewardService.calculateRewards(customerId, from, to);
        return ResponseEntity.ok(response);
    }
}
