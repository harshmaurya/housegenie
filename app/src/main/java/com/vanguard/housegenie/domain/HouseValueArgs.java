package com.vanguard.housegenie.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HouseValueArgs {

    private final HouseBuyDetails houseBuyDetails;
    private final RentParameters rentParameters;
    private final ITaxDetails taxDetails;
    private final LocalDate startDate;
    private final BigDecimal inflationRate;

    public HouseValueArgs(HouseBuyDetails houseBuyDetails, RentParameters rentParameters,
                          ITaxDetails taxDetails, LocalDate startDate, BigDecimal inflationRate) {
        this.houseBuyDetails = houseBuyDetails;
        this.rentParameters = rentParameters;
        this.taxDetails = taxDetails;
        this.startDate = startDate;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public BigDecimal getInflationRate() {
        return inflationRate;
    }
}
