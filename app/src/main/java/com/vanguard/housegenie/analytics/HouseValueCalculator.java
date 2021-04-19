package com.vanguard.housegenie.analytics;

import com.vanguard.housegenie.domain.HouseBuyDetails;
import com.vanguard.housegenie.domain.LoanDetails;
import com.vanguard.housegenie.domain.RentParameters;
import com.vanguard.housegenie.domain.TaxDetails;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HouseValueCalculator {

    private static final MathContext precision = MathContext.DECIMAL32;
    private static final int months = 12;

    private Date addMonths(Date referenceDate, int monthsToAdd){
        Calendar c = Calendar.getInstance();
        c.setTime(referenceDate);
        c.add(Calendar.MONTH, monthsToAdd);
        return c.getTime();
    }

    public BigDecimal getXirr(HouseBuyDetails houseBuyDetails, RentParameters rentDetails, TaxDetails taxDetails){

        LoanDetails loanDetails = houseBuyDetails.getLoanDetails();
        int years = loanDetails.getTerm();

        ArrayList<CashFlow> txns = new ArrayList<>();

        Date startDate = new Date();

        txns.add(new CashFlow(houseBuyDetails.getDownPayment().multiply(BigDecimal.valueOf(-1)), startDate));

        BigDecimal balanceLeft = loanDetails.getPrincipal();
        BigDecimal emi = getEmi(loanDetails);
        BigDecimal monthlyInterestRate = (loanDetails.getInterestRate().divide(BigDecimal.valueOf(months), precision))
                .divide(BigDecimal.valueOf(100), precision);
        BigDecimal payment = emi;
        BigDecimal initialRent = rentDetails.getInitialRent();
        BigDecimal rate = rentDetails.getAnnualAppreciation()
                .divide(BigDecimal.valueOf(100), precision);
        for (int i = 1; i <= years * months; i++) {

            BigDecimal interestComponent = (balanceLeft.multiply(monthlyInterestRate));
            BigDecimal principalComponent = balanceLeft.min(payment.subtract(interestComponent));
            if(balanceLeft.compareTo(emi) > 0){
                payment = emi;
            }
            else {
                payment = interestComponent.add(balanceLeft);
            }

            if(payment.compareTo(BigDecimal.valueOf(0)) > 0){
                balanceLeft = balanceLeft.subtract(principalComponent);
            }
            else {
                interestComponent = BigDecimal.valueOf(0);
                balanceLeft = BigDecimal.valueOf(0);
            }

            BigDecimal taxSavings = interestComponent.multiply(taxDetails.getTaxRate())
                    .divide(BigDecimal.valueOf(100));

            int periods = i / months;
            BigDecimal rent = (initialRent.multiply((BigDecimal.valueOf(1).add(rate)).pow(periods)));

            BigDecimal netIncome = rent.add(taxSavings).subtract(emi);
            txns.add(new CashFlow(netIncome, addMonths(startDate, i)));
        }
        BigDecimal houseFutureValue = calculateHouseValue(houseBuyDetails);
        txns.add(new CashFlow(houseFutureValue, addMonths(startDate, years*months)));

        XirrResult result = XirrCalculator.calculate(txns);

        return result.getXirr();
    }

    public HouseFutureValue getFutureValue(HouseBuyDetails houseBuyDetails, TaxDetails taxDetails, BigDecimal reinvestmentRate){

        BigDecimal costOfEmi = calculateCostOfEmi(houseBuyDetails, taxDetails, reinvestmentRate);
        BigDecimal houseValue = calculateHouseValue(houseBuyDetails);
        BigDecimal downPaymentCost = calculateDownPaymentCost(houseBuyDetails, reinvestmentRate);
        return new HouseFutureValue(houseValue,costOfEmi,downPaymentCost);
    }

    private static BigDecimal calculateCostOfEmi(HouseBuyDetails houseBuyDetails, TaxDetails taxDetails, BigDecimal reinvestmentRate)
    {
        LoanDetails loanDetails = houseBuyDetails.getLoanDetails();
        BigDecimal emi = getEmi(loanDetails);
        BigDecimal monthlyInterestRate = (loanDetails.getInterestRate().divide(BigDecimal.valueOf(months), precision))
                .divide(BigDecimal.valueOf(100), precision);
        BigDecimal monthlyInvestmentRate = (reinvestmentRate.divide(BigDecimal.valueOf(months), precision))
                .divide(BigDecimal.valueOf(100), precision);
        BigDecimal balanceLeft = loanDetails.getPrincipal();
        BigDecimal payment = emi;
        BigDecimal costOfEmi = BigDecimal.valueOf(0);
        int years = loanDetails.getTerm();
        for (int i = 1; i <= years * months; i++) {

            BigDecimal interestComponent = (balanceLeft.multiply(monthlyInterestRate));
            BigDecimal principalComponent = balanceLeft.min(payment.subtract(interestComponent));

            if(balanceLeft.compareTo(emi) > 0){
                payment = emi;
            }
            else {
                payment = interestComponent.add(balanceLeft);
            }

            if(payment.compareTo(BigDecimal.valueOf(0)) > 0){
                balanceLeft = balanceLeft.subtract(principalComponent);
            }
            else {
                interestComponent = BigDecimal.valueOf(0);
                balanceLeft = BigDecimal.valueOf(0);
            }

            BigDecimal taxSavings = interestComponent.multiply(taxDetails.getTaxRate())
                    .divide(BigDecimal.valueOf(100));
            BigDecimal netEmi = payment.subtract(taxSavings);
            BigDecimal emiFutureValue = FinancialCalculator.futureValue(netEmi,monthlyInvestmentRate, (years*months)-i);
            costOfEmi = costOfEmi.add(emiFutureValue);
        }
        return costOfEmi.add(balanceLeft);
    }

    private static BigDecimal calculateHouseValue(HouseBuyDetails houseBuyDetails){

        BigDecimal initialValue = houseBuyDetails.getDownPayment().add(houseBuyDetails.getLoanDetails().getPrincipal());
        BigDecimal rateOfAppreciation = houseBuyDetails.getHouseAppreciationRate();
        int years = houseBuyDetails.getLoanDetails().getTerm();


        return calculateFutureValueMonthlyCompounding(initialValue, rateOfAppreciation, years);
    }


    private static BigDecimal calculateDownPaymentCost(HouseBuyDetails houseBuyDetails, BigDecimal reinvestmentRate){
        BigDecimal initialValue = houseBuyDetails.getDownPayment();
        BigDecimal rateOfAppreciation = reinvestmentRate;
        int years = houseBuyDetails.getLoanDetails().getTerm();


        return calculateFutureValueMonthlyCompounding(initialValue, rateOfAppreciation, years);
    }

    private static BigDecimal calculateFutureValueMonthlyCompounding(BigDecimal initialValue, BigDecimal rateOfAppreciation, int years) {
        BigDecimal rate = (rateOfAppreciation.divide(BigDecimal.valueOf(months), precision))
                .divide(BigDecimal.valueOf(100), precision);

        int periods = years * months;

        return FinancialCalculator.futureValue(initialValue, rate, periods);
    }

    private static BigDecimal getEmi(LoanDetails loan){
        BigDecimal loanPrincipal = loan.getPrincipal().multiply(BigDecimal.valueOf(-1));
        BigDecimal interest = loan.getInterestRate();
        int term = loan.getTerm();
        BigDecimal interestPerPayment = (interest.divide(BigDecimal.valueOf(months), precision))
                .divide(BigDecimal.valueOf(100),precision);

        return FinancialCalculator.pmt(interestPerPayment, months * term, loanPrincipal, false);
    }
}

