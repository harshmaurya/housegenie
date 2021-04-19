package com.vanguard.housegenie.domain;

import java.math.BigDecimal;


public class RentVsBuyArgs {

    private final HouseBuyDetails houseBuyDetails;
    private final RentParameters rentParameters;
    private final TaxDetails taxDetails;
    private final BigDecimal inflationRate;

    public RentVsBuyArgs(HouseBuyDetails houseBuyDetails, RentParameters rentParameters,
                         TaxDetails taxDetails, BigDecimal inflationRate){
        this.houseBuyDetails = houseBuyDetails;
        this.rentParameters = rentParameters;
        this.taxDetails = taxDetails;
        this.inflationRate = inflationRate;
    }

    public RentParameters getRentParameters() {
        return rentParameters;
    }

    public HouseBuyDetails getHouseBuyDetails() {
        return houseBuyDetails;
    }

    public TaxDetails getTaxDetails() {
        return taxDetails;
    }

    public BigDecimal getInflationRate() {
        return inflationRate;
    }
}

