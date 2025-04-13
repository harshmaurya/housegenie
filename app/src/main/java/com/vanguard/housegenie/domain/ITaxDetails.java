package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public interface ITaxDetails {
     BigDecimal calculateTaxSavings(BigDecimal yearlyInterestAmount,
                                    BigDecimal yearlyMaintenanceCost, BigDecimal rentalIncome);
}

