package com.vanguard.housegenie.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HouseValueArgs {

    private final HouseBuyDetails houseBuyDetails;
    private final RentParameters rentParameters;
    private final TaxDetails taxDetails;
    private final LocalDate startDate;

    public HouseValueArgs(HouseBuyDetails houseBuyDetails, RentParameters rentParameters,
                          TaxDetails taxDetails, LocalDate startDate) {
        this.houseBuyDetails = houseBuyDetails;
        this.rentParameters = rentParameters;
        this.taxDetails = taxDetails;
        this.startDate = startDate;
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

    public LocalDate getStartDate() {
        return startDate;
    }
}
