package com.vanguard.housegenie.models;

import com.vanguard.housegenie.analytics.FinancialCalculator;
import com.vanguard.housegenie.analytics.HouseValueCalculator;
import com.vanguard.housegenie.domain.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HouseValueModel {

    private final HouseValueCalculator houseValueCalculator;

    public HouseValueModel() {
        this.houseValueCalculator = new HouseValueCalculator();
    }

    public HouseValueResult CalculateHouseValue(HouseValueArgs args) {
        LoanDetails loanDetails = args.getHouseBuyDetails().getLoanDetails();
        int monthsPassed = (int) ChronoUnit.MONTHS.between(args.getStartDate().withDayOfMonth(1), LocalDate.now().withDayOfMonth(1));
        double years = (double) monthsPassed / 12;
        BigDecimal houseXirr = houseValueCalculator.getXirr(args.getHouseBuyDetails(), args.getRentParameters(),
                args.getTaxDetails(), args.getStartDate(), args.getInflationRate());
        BigDecimal investmentAmount = args.getHouseBuyDetails().getDownPayment().add(loanDetails.getPrincipal());
        BigDecimal valueFromHouseXirr = FinancialCalculator.futureValue(investmentAmount, houseXirr, years);
        return new HouseValueResult(houseXirr,monthsPassed, investmentAmount, valueFromHouseXirr);
    }
}
