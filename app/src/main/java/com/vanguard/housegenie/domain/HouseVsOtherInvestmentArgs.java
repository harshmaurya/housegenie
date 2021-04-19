package com.vanguard.housegenie.domain;

import java.math.BigDecimal;

public class HouseVsOtherInvestmentArgs {
    private final HouseBuyDetails houseBuyDetails;
    private final RentParameters rentParameters;
    private final TaxDetails taxDetails;
    private final BigDecimal investmentReturn;

    public HouseVsOtherInvestmentArgs(HouseBuyDetails houseBuyDetails, RentParameters rentParameters,
                                      TaxDetails taxDetails, BigDecimal investmentReturn) {
        this.houseBuyDetails = houseBuyDetails;
        this.rentParameters = rentParameters;
        this.taxDetails = taxDetails;
        this.investmentReturn = investmentReturn;
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

    public BigDecimal getInvestmentReturn() {
        return investmentReturn;
    }
}
