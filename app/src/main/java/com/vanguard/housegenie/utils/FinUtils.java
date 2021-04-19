package com.vanguard.housegenie.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class FinUtils {

    public static String convertToCurrencyFormat(BigDecimal input){
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format(input);
    }

    public static String convertToRateFormat(BigDecimal input){
        BigDecimal res = input.setScale(1, BigDecimal.ROUND_HALF_DOWN);
        return res + "%";
    }
}
