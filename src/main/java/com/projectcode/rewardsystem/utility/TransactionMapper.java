package com.projectcode.rewardsystem.utility;

import com.projectcode.rewardsystem.dto.TransactionDTO;
import com.projectcode.rewardsystem.model.Transaction;

import java.util.List;

public class TransactionMapper {
    public static TransactionDTO toDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getId(),transaction.getAmount(),transaction.getDate());
    }

    public static List<TransactionDTO> toDTOList(List<Transaction> transactions){
        return transactions.stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }
}
