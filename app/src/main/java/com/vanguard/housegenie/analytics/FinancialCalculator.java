package com.vanguard.housegenie.analytics;

import java.math.BigDecimal;
import java.math.MathContext;

public class FinancialCalculator {

    /**
     * PMT function ported from Excel to Java to use BigDecimals.
     * @param interestRate                   interest rate for the loan.
     * @param numberOfPayments               is the total number of payments for the loan.
     * @param principal                      is the present value; also known as the principal.
     * @param paymentsDueAtBeginningOfPeriod payments are due at the beginning of the period.
     * @return payment
     * @see <a href="https://apache.googlesource.com/poi/+/4d81d34d5d566cb22f21999e653a5829cc678ed5/src/java/org/apache/poi/ss/formula/functions/FinanceLib.java#143">FincanceLib</a>
     */
    public static BigDecimal pmt(BigDecimal interestRate,
                                 int numberOfPayments,
                                 BigDecimal principal,
                                 boolean paymentsDueAtBeginningOfPeriod) {

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

    public static BigDecimal futureValue(BigDecimal presentValue, BigDecimal rate, int periods){
        final BigDecimal r1 = rate.add(BigDecimal.ONE);
        final BigDecimal pow = r1.pow(periods);
        return presentValue.multiply(pow);
    }

    public static BigDecimal futureValue(BigDecimal presentValue, int periods, BigDecimal rateInPercentage){
        BigDecimal rate = rateInPercentage.divide(BigDecimal.valueOf(100), MathContext.DECIMAL32);
        return futureValue(presentValue,rate, periods);
    }
}
