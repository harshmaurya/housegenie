package com.vanguard.housegenie.models;

import com.vanguard.housegenie.analytics.FinancialCalculator;
import com.vanguard.housegenie.analytics.HouseFutureValue;
import com.vanguard.housegenie.analytics.HouseValueCalculator;
import com.vanguard.housegenie.analytics.RentValueCalculator;
import com.vanguard.housegenie.domain.HouseVsOtherInvestmentArgs;
import com.vanguard.housegenie.domain.HouseVsOtherInvestmentResult;
import com.vanguard.housegenie.domain.LoanDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class HouseVsOtherInvestmentModel {

    private final HouseValueCalculator houseValueCalculator;

    public HouseVsOtherInvestmentModel() {
        this.houseValueCalculator = new HouseValueCalculator();
    }

    public HouseVsOtherInvestmentResult CompareInvestments(HouseVsOtherInvestmentArgs args) {

        LoanDetails loanDetails = args.getHouseBuyDetails().getLoanDetails();
        int term = loanDetails.getTerm();
        BigDecimal houseXirr = houseValueCalculator.getXirr(args.getHouseBuyDetails(), args.getRentParameters(), args.getTaxDetails());
        BigDecimal investmentXirr = args.getInvestmentReturn();
        BigDecimal investmentAmount = args.getHouseBuyDetails().getDownPayment().add(loanDetails.getPrincipal());
        BigDecimal valueFromInvestmentXirr = FinancialCalculator.futureValue(investmentAmount, investmentXirr, term);
        BigDecimal valueFromHouseXirr = FinancialCalculator.futureValue(investmentAmount, houseXirr, term);

        return new HouseVsOtherInvestmentResult(houseXirr, investmentXirr, term, investmentAmount, valueFromHouseXirr, valueFromInvestmentXirr);
    }
}

