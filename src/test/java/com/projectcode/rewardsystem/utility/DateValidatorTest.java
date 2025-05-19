package com.projectcode.rewardsystem.utility;

import com.projectcode.rewardsystem.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class DateValidatorTest {

    @Test
    @DisplayName("should not throw exception when valid date range")
    public void testValidateDateRange_ValidDateRange(){
        LocalDate fromDate = LocalDate.now().minusMonths(2);
        LocalDate toDate = LocalDate.now();

        assertDoesNotThrow(() ->DateValidator.validateDateRange(fromDate, toDate));
    }

    @Test
    @DisplayName("should throw InvalidParameterException when fromDate is in the future")
    public void testValidateDateRange_FutureFromDate(){
        LocalDate fromDate = LocalDate.now().plusDays(1);
        LocalDate toDate = LocalDate.now().plusMonths(3);

        InvalidParameterException ex =
                assertThrows(InvalidParameterException.class,
                        () -> DateValidator.validateDateRange(fromDate, toDate));

        assertEquals("fromDate cannot be in future. Please provide valid date", ex.getMessage());

    }

    @Test
    @DisplayName("should throw InvalidParameterException when toDate is in the future")
    public void testValidateDateRange_FutureToDate(){
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now().plusMonths(3);

        InvalidParameterException ex =
                assertThrows(InvalidParameterException.class,
                        () -> DateValidator.validateDateRange(fromDate, toDate));

        assertEquals("toDate cannot be in future. Please provide valid date", ex.getMessage());

    }

    @Test
    @DisplayName("should throw InvalidParameterException when fromDate is after toDate")
    public void testValidateDateRange_FromDateIsAfterToDate(){
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now().minusMonths(3);

        InvalidParameterException ex =
                assertThrows(InvalidParameterException.class,
                        () -> DateValidator.validateDateRange(fromDate, toDate));

        assertEquals("fromDate cannot be after toDate. Please provide valid dates.", ex.getMessage());

    }

    @Test
    @DisplayName("should normalize toDate when it is null and fromDate is provided")
    public void testNormalizeDateInputs_NullToDate(){
        LocalDate fromDate = LocalDate.now().minusMonths(3);
        LocalDate expectedToDate = LocalDate.now();

        DateValidator.DateRange dateRange = DateValidator.normalizeDateInputs(fromDate, null);

        assertEquals(fromDate, dateRange.fromDate());
        assertEquals(expectedToDate, dateRange.toDate());

    }

    @Test
    @DisplayName("should normalize fromDate when it is null and toDate is provided")
    public void testNormalizeDateInputs_NullFromDate(){
        LocalDate toDate = LocalDate.now();
        LocalDate expectedFromDate = toDate.minusMonths(3);

        DateValidator.DateRange dateRange = DateValidator.normalizeDateInputs(null, toDate);

        assertEquals(expectedFromDate, dateRange.fromDate());
        assertEquals(toDate, dateRange.toDate());

    }

    @Test
    @DisplayName("should normalize fromDate and toDate when both are null")
    public void testNormalizeDateInputs_BothAreNull(){
        DateValidator.DateRange dateRange = DateValidator.normalizeDateInputs(null, null);

        LocalDate expectedToDate = LocalDate.now();
        LocalDate expectedFromDate = expectedToDate.minusMonths(3);

        assertEquals(expectedFromDate, dateRange.fromDate());
        assertEquals(expectedToDate, dateRange.toDate());

    }

}
