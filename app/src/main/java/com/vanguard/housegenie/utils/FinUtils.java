package com.vanguard.housegenie.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import static com.vanguard.housegenie.analytics.FinancialCalculator.toPercentageRate;

public class FinUtils {

    public static String convertToCurrencyFormat(BigDecimal input) {
        String country = AppSession.getSelectedCountry();
        Locale locale = CurrencyCountryMapping.getCountryToLocaleMapping().getOrDefault(country, Locale.getDefault());
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format(input);
    }

    public static String convertToRateFormat(BigDecimal fractionalRate) {
        BigDecimal res = toPercentageRate(fractionalRate).setScale(1, BigDecimal.ROUND_HALF_DOWN);
        return res + "%";
    }
}
