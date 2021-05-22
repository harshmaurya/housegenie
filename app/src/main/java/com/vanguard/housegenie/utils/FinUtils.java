package com.vanguard.housegenie.utils;

import com.vanguard.housegenie.analytics.FinancialCalculator;

import java.math.BigDecimal;
import java.text.NumberFormat;

import static com.vanguard.housegenie.analytics.FinancialCalculator.toPercentageRate;

public class FinUtils {

    public static String convertToCurrencyFormat(BigDecimal input){
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format(input);
    }

    public static String convertToRateFormat(BigDecimal fractionalRate){
        BigDecimal res = toPercentageRate(fractionalRate).setScale(1, BigDecimal.ROUND_HALF_DOWN);
        return res + "%";
    }
}
