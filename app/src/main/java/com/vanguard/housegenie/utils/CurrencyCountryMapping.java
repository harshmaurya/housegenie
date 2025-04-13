package com.vanguard.housegenie.utils;

import java.util.*;

public class CurrencyCountryMapping {

    // Map for Country Name -> Currency Code
    private static final Map<String, String> countryToCurrency = new HashMap<>();
    private static final Map<String, Locale> countryToLocale = new HashMap<>();
    private static Map<String, Locale> processedCountries = new HashMap<>();

    // Static initializer block
    static {
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                // Ensure the locale has a display country
                if (!locale.getDisplayCountry().isEmpty()) {
                    String countryName = locale.getDisplayCountry();
                    Currency currency = Currency.getInstance(locale);

                    if (currency != null) {
                        String currencyCode = currency.getCurrencyCode();

                        // Check if the country has already been processed
                        if (!processedCountries.containsKey(countryName)) {
                            // If not yet processed, add this locale
                            processedCountries.put(countryName, locale);
                            countryToCurrency.put(countryName, currencyCode);
                            countryToLocale.put(countryName, locale);
                        } else if (locale.getLanguage().equals("en")) {
                            // Prioritize "en" locales if already processed
                            countryToLocale.put(countryName, locale);
                            countryToCurrency.put(countryName, currencyCode);
                        }
                    }
                }
            } catch (Exception e) {
                // Skip invalid locales
            }
        }
    }

    public static Map<String, String> getCountryToCurrencyMapping() {
        return countryToCurrency;
    }

    public static Map<String, Locale> getCountryToLocaleMapping() {
        return countryToLocale;
    }
}
