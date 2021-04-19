package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class RentParameters {

    private final BigDecimal initialValue;
    private final BigDecimal annualAppreciation;

    public RentParameters(BigDecimal initialRent, BigDecimal annualIncrementRate){
        this.initialValue = initialRent;
        this.annualAppreciation = annualIncrementRate;
    }

    public BigDecimal getInitialRent() {
        return initialValue;
    }

    public BigDecimal getAnnualAppreciation(){
        return this.annualAppreciation;
    }
}
