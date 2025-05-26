package com.projectcode.rewardsystem.utility;

import com.projectcode.rewardsystem.dto.TransactionDTO;
import com.projectcode.rewardsystem.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionMapperTest {

    @Test
    @DisplayName("should map Transaction to TransactionDTO")
    public void testToDTO_ValidTransaction(){
        Transaction transaction = new Transaction(1L, 1L, 53.0, LocalDate.now());

        TransactionDTO transactionDTO = TransactionMapper.toDTO(transaction);

        assertEquals(1L, transactionDTO.getTransactionId());
        assertEquals(53D, transactionDTO.getAmount());
        assertEquals(LocalDate.now(), transactionDTO.getDate());

    }

    @Test
    @DisplayName("should return empty TransactionDTO for empty Transaction")
    public void testToDTO_EmptyTransaction(){
        Transaction transaction = new Transaction();

        TransactionDTO transactionDTO = TransactionMapper.toDTO(transaction);

        assertNull(transactionDTO.getTransactionId());
        assertNull(transactionDTO.getAmount());
        assertNull(transactionDTO.getDate());
    }

    @Test
    @DisplayName("should return List of TransactionDTO for the given Transactions")
    public void testToDTO_ListOfTransactions(){
        Transaction transaction1 = new Transaction(1L, 1L, 90.0, LocalDate.now());
        Transaction transaction2 = new Transaction(2L, 1L, 150.0, LocalDate.now().minusMonths(1));

        List<TransactionDTO> transactionDTOList = TransactionMapper.toDTOList(Arrays.asList(transaction1, transaction2));

        assertNotNull(transactionDTOList);
        assertEquals(2, transactionDTOList.size());

        // Test first transaction
        assertEquals(transaction1.getId(), transactionDTOList.get(0).getTransactionId());
        assertEquals(transaction1.getAmount(), transactionDTOList.get(0).getAmount());
        assertEquals(transaction1.getDate(), transactionDTOList.get(0).getDate());

        // Test second transaction
        assertEquals(transaction2.getId(), transactionDTOList.get(1).getTransactionId());
        assertEquals(transaction2.getAmount(), transactionDTOList.get(1).getAmount());
        assertEquals(transaction2.getDate(), transactionDTOList.get(1).getDate());
    }

    @Test
    @DisplayName("should return empty List of TransactionDTO for the empty Transactions")
    public void testToDTO_EmptyListOfTransactions(){

        List<TransactionDTO> transactionDTOList = TransactionMapper.toDTOList(List.of());
        assertTrue(transactionDTOList.isEmpty());

    }

}
