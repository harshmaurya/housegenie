package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class HouseBuyDetails {

    private final BigDecimal downPayment;
    private final LoanDetails loanDetails;
    private final BigDecimal houseAppreciationRate;

    public HouseBuyDetails(BigDecimal downPayment, LoanDetails loanDetails, BigDecimal houseAppreciationRate) {
        this.downPayment = downPayment;
        this.loanDetails = loanDetails;
        this.houseAppreciationRate = houseAppreciationRate;
    }

    public BigDecimal getDownPayment(){
        return this.downPayment;
    }

    public LoanDetails getLoanDetails(){
        return this.loanDetails;
    }

    public BigDecimal getHouseAppreciationRate() {
        return houseAppreciationRate;
    }
}
