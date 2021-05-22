package com.vanguard.housegenie.analytics;

import java.math.BigDecimal;
import java.math.MathContext;

public class FinancialCalculator {

    private static final MathContext precision = MathContext.DECIMAL32;

    public static BigDecimal pmt(BigDecimal interestRate,
                                 int numberOfPayments,
                                 BigDecimal principal,
                                 boolean paymentsDueAtBeginningOfPeriod) {

        if(principal.equals(BigDecimal.ZERO)) return BigDecimal.ZERO;
        if(numberOfPayments==0) return BigDecimal.ZERO;
        BigDecimal futureValue = BigDecimal.valueOf(0);
        final BigDecimal n = new BigDecimal(numberOfPayments);
        if (BigDecimal.ZERO.equals(interestRate)) {
            return (futureValue.add(principal)).divide(n, MathContext.DECIMAL128).negate();
        } else {
            final BigDecimal r1 = interestRate.add(BigDecimal.ONE);
            final BigDecimal pow = r1.pow(numberOfPayments);

            final BigDecimal divisor;
            if (paymentsDueAtBeginningOfPeriod) {
                divisor = r1.multiply(BigDecimal.ONE.subtract(pow));
            } else {
                divisor = BigDecimal.ONE.subtract(pow);
            }
            return (principal.multiply(pow).add(futureValue)).multiply(interestRate).divide(divisor, MathContext.DECIMAL128);
        }
    }

    public static BigDecimal futureValue(BigDecimal presentValue, BigDecimal rate, double periods){
        final BigDecimal r1 = rate.add(BigDecimal.ONE);
        final double pow = Math.pow(r1.doubleValue(), periods);
        return presentValue.multiply(BigDecimal.valueOf(pow));
    }

    public static BigDecimal getRate(BigDecimal presentValue, BigDecimal futureValue, double periods){
        BigDecimal ratio = futureValue.divide(presentValue, precision);
        double rate = Math.pow(ratio.doubleValue(), (1/periods)) - 1;
        return BigDecimal.valueOf(rate);
    }

    // To change compounding from annual to monyhly, set periods = 12
    public static double changeCompounding(double rate, double periods){
        return (Math.pow((rate + 1),1/periods)-1)*periods;
    }

    public static BigDecimal toPercentageRate(BigDecimal fractionalRate){
        return fractionalRate.multiply(BigDecimal.valueOf(100));
    }

    public static BigDecimal toFractionalRate(BigDecimal rate){
        return rate.divide(BigDecimal.valueOf(100), precision);
    }
}
