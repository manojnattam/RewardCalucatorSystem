package com.projectcode.rewardsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotNull(message = "Date is required")
    private LocalDate date;

}