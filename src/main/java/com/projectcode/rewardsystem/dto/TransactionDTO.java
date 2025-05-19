package com.projectcode.rewardsystem.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TransactionDTO {
    private Long transactionId;
    private Double amount;
    private LocalDate date;
}
