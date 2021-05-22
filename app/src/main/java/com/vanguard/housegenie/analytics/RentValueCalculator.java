package com.vanguard.housegenie.analytics;

import com.vanguard.housegenie.domain.RentParameters;

import java.math.BigDecimal;
import java.math.MathContext;

public class RentValueCalculator {
    private static final MathContext precision = MathContext.DECIMAL32;
    private static final int months = 12;

    public BigDecimal getRentFutureValue(RentParameters rentParameters, int years, BigDecimal reinvestmentRate){

        BigDecimal initialRent = rentParameters.getInitialRent();
        BigDecimal rate = rentParameters.getAnnualAppreciation();
        BigDecimal totalFutureValue = BigDecimal.valueOf(0);
        for (int i = 1; i <= years * months; i++) {
            int periods = i / months;
            BigDecimal rent = (initialRent.multiply((BigDecimal.valueOf(1).add(rate)).pow(periods)));
            double periodInYears = ((double)(years*months - i))/months;
            BigDecimal futureValueOfRent = FinancialCalculator.futureValue(rent, reinvestmentRate, periodInYears);
            totalFutureValue = totalFutureValue.add(futureValueOfRent);
        }
        return totalFutureValue;
    }
}
