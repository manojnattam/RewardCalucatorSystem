package com.projectcode.rewardsystem.utility;

import com.projectcode.rewardsystem.exception.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DateValidator {
    private static final Logger log = LoggerFactory.getLogger(DateValidator.class);
    public static void validateDateRange(LocalDate fromDate, LocalDate toDate){
        LocalDate today = LocalDate.now();

        if(fromDate != null && fromDate.isAfter(today)){
            log.error("fromDate {} is in the future.",fromDate);
            throw new InvalidParameterException("fromDate cannot be in future. Please provide valid date");
        }
        if(toDate != null && toDate.isAfter(today)){
            log.error("toDate {} is in the future", toDate);
            throw new InvalidParameterException("toDate cannot be in future. Please provide valid date");
        }
        if(fromDate != null && toDate != null && fromDate.isAfter(toDate)){
            log.error("fromDate {} is after toDate {}.", fromDate, toDate);
            throw new InvalidParameterException("fromDate cannot be after toDate. Please provide valid dates.");
        }
    }

    public static DateRange normalizeDateInputs(LocalDate fromDate, LocalDate toDate){
        if(fromDate == null ) {
            if(toDate != null)
                fromDate = toDate.minusMonths(3);
            else {
                toDate = LocalDate.now();
                fromDate = toDate.minusMonths(3);
            }
        } else if(toDate == null) {
            toDate = LocalDate.now();
        }
        return new DateRange(fromDate, toDate);
    }
    public record DateRange(LocalDate fromDate, LocalDate toDate) {}
}
