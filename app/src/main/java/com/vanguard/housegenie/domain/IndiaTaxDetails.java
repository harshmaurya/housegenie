package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class IndiaTaxDetails implements ITaxDetails {

    private final BigDecimal taxRate;
    private final BigDecimal interestCap;

    public IndiaTaxDetails(BigDecimal taxRate, BigDecimal interestCap){
        this.taxRate = taxRate;
        this.interestCap = interestCap;
    }

    @Override
    public BigDecimal calculateTaxSavings(BigDecimal yearlyInterestAmount,
                                          BigDecimal yearlyMaintenanceCost,
                                          BigDecimal rentalIncome) {
        BigDecimal netInterest = yearlyInterestAmount.compareTo(interestCap) > 0 ? interestCap : yearlyInterestAmount;
        return netInterest.multiply(taxRate);
    }
}

