package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class TaxDetails {

    private final BigDecimal taxRate;
    private final BigDecimal interestCap;

    public TaxDetails(BigDecimal taxRate, BigDecimal interestCap){
        this.taxRate = taxRate;
        this.interestCap = interestCap;
    }

    public BigDecimal getTaxRate(){
        return taxRate;
    }

    public BigDecimal getInterestCap() {
        return interestCap;
    }
}
