package com.vanguard.housegenie.models;

public class CountryCurrency {
    private String country;
    private String currencyCode;

    public CountryCurrency(String country, String currencyCode) {
        this.country = country;
        this.currencyCode = currencyCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
