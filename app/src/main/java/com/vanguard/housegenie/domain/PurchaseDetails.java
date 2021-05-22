package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class PurchaseDetails {
    private final BigDecimal downPayment;
    private final LoanDetails loanDetails;

    public PurchaseDetails(BigDecimal downPayment, LoanDetails loanDetails) {
        this.downPayment = downPayment;
        this.loanDetails = loanDetails;
    }

    public BigDecimal getDownPayment() {
        return this.downPayment;
    }

    public LoanDetails getLoanDetails() {
        return this.loanDetails;
    }
}
