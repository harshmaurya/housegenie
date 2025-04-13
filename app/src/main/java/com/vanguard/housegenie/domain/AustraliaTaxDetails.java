package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class AustraliaTaxDetails implements ITaxDetails {

    private final BigDecimal marginalTaxRate;

    public AustraliaTaxDetails(BigDecimal marginalTaxRate) {
        this.marginalTaxRate = marginalTaxRate;
    }

    @Override
    public BigDecimal calculateTaxSavings(BigDecimal yearlyInterestAmount, BigDecimal yearlyMaintenanceCost,
                                          BigDecimal rentalIncome) {
        if (rentalIncome.equals(BigDecimal.ZERO)) {
            // Self living scenario, no tax rebate applicable
            return BigDecimal.ZERO;
        }
        BigDecimal netCost = yearlyMaintenanceCost.add(yearlyInterestAmount).subtract(rentalIncome);
        if (netCost.compareTo(BigDecimal.ZERO) > 0) {
            // negatively geared
            return netCost.multiply(marginalTaxRate);
        }
        return BigDecimal.ZERO;
    }
}