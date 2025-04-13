package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class HouseBuyDetails {

    private final BigDecimal downPayment;
    private final LoanDetails loanDetails;
    private final BigDecimal houseAppreciationRate;
    private final BigDecimal annualMaintenance;

    public HouseBuyDetails(BigDecimal downPayment, LoanDetails loanDetails,
                           BigDecimal annualHouseAppreciation, BigDecimal annualMaintenance) {
        this.downPayment = downPayment;
        this.loanDetails = loanDetails;
        this.houseAppreciationRate = annualHouseAppreciation;
        this.annualMaintenance = annualMaintenance;
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

    public BigDecimal getAnnualMaintenance() {
        return annualMaintenance;
    }
}

