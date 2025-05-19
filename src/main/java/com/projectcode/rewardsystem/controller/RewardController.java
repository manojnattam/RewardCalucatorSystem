package com.projectcode.rewardsystem.controller;

import com.projectcode.rewardsystem.dto.RewardResponse;
import com.projectcode.rewardsystem.service.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {
    private static final Logger log = LoggerFactory.getLogger(RewardController.class);

    @Autowired
    private RewardService rewardService;

    @GetMapping("/{customerId}")
    public ResponseEntity<RewardResponse> getRewards(
            @PathVariable Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        log.info("Received request: customerId: {} fromDate: {} toDate: {}",customerId,fromDate, toDate);

        RewardResponse response = rewardService.calculateRewards(customerId, fromDate, toDate);

        log.info("Returning response: "+response);
        return ResponseEntity.ok(response);
    }
}
