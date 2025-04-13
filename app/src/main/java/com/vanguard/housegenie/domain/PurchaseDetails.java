package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class PurchaseDetails {
    private final BigDecimal downPayment;
    private final LoanDetails loanDetails;
    private final BigDecimal maintenance;

    public PurchaseDetails(BigDecimal downPayment, LoanDetails loanDetails, BigDecimal maintenance) {
        this.downPayment = downPayment;
        this.loanDetails = loanDetails;
        this.maintenance = maintenance;
    }

    public PurchaseDetails(BigDecimal downPayment, LoanDetails loanDetails) {
        this(downPayment, loanDetails, BigDecimal.ZERO);
    }

    public BigDecimal getDownPayment() {
        return this.downPayment;
    }

    public LoanDetails getLoanDetails() {
        return this.loanDetails;
    }

    public BigDecimal getMaintenance() {
        return maintenance;
    }
}
