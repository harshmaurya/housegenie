package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class LoanDetails {

    private final BigDecimal principal;
    private final int term;
    private final BigDecimal interestRate;

    public LoanDetails(BigDecimal principal, int term, BigDecimal interestRate){
        this.principal = principal;
        this.term = term;
        this.interestRate = interestRate;
    }

    public BigDecimal getPrincipal(){
        return this.principal;
    }

    public int getTerm(){
        return this.term;
    }

    public BigDecimal getInterestRate(){
        return this.interestRate;
    }
}

