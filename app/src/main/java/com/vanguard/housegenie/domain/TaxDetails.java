package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class TaxDetails {

    private final BigDecimal taxRate;

    public TaxDetails(BigDecimal taxRate){
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxRate(){
        return taxRate;
    }
}
