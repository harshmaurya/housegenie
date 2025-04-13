package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class EmptyTaxDetails implements ITaxDetails{

    @Override
    public BigDecimal calculateTaxSavings(BigDecimal yearlyInterestAmount, BigDecimal yearlyMaintenanceCost, BigDecimal rentalIncome) {
        return BigDecimal.ZERO;
    }
}
