package com.vanguard.housegenie.utils;

import java.util.Locale;

public class AppSession {
    private static String selectedCountry = Locale.getDefault().getDisplayCountry();

    public static void setSelectedCountry(String country) {
        selectedCountry = country;
    }

    public static String getSelectedCountry() {
        return selectedCountry;
    }
}
