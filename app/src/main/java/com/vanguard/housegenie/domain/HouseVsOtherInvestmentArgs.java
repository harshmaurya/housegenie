package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class HouseVsOtherInvestmentArgs {
    private final HouseBuyDetails houseBuyDetails;
    private final RentParameters rentParameters;
    private final ITaxDetails taxDetails;
    private final BigDecimal investmentReturn;
    private final BigDecimal inflationRate;

    public HouseVsOtherInvestmentArgs(HouseBuyDetails houseBuyDetails, RentParameters rentParameters,
                                      ITaxDetails taxDetails, BigDecimal investmentReturn, BigDecimal inflationRate) {
        this.houseBuyDetails = houseBuyDetails;
        this.rentParameters = rentParameters;
        this.taxDetails = taxDetails;
        this.investmentReturn = investmentReturn;
        this.inflationRate = inflationRate;
    }

    public RentParameters getRentParameters() {
        return rentParameters;
    }

    public HouseBuyDetails getHouseBuyDetails() {
        return houseBuyDetails;
    }

    public ITaxDetails getTaxDetails() {
        return taxDetails;
    }

    public BigDecimal getInvestmentReturn() {
        return investmentReturn;
    }

    public BigDecimal getInflationRate() {
        return inflationRate;
    }
}

