package com.vanguard.housegenie.analytics;

import com.vanguard.housegenie.domain.HouseBuyDetails;
import com.vanguard.housegenie.domain.LoanDetails;
import com.vanguard.housegenie.domain.RentParameters;
import com.vanguard.housegenie.domain.TaxDetails;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class HouseValueCalculator {

    private static final MathContext precision = MathContext.DECIMAL32;
    private static final int months = 12;

    private LocalDate addMonths(LocalDate referenceDate, long monthsToAdd){
        return referenceDate.plusMonths(monthsToAdd);
    }

    public BigDecimal getXirr(HouseBuyDetails houseBuyDetails,
                              RentParameters rentDetails,
                              TaxDetails taxDetails){

        LoanDetails loanDetails = houseBuyDetails.getLoanDetails();
        int periods = loanDetails.getTerm() * months;
        LocalDate startDate = LocalDate.now().minusMonths(periods);

        return getXirr(houseBuyDetails, rentDetails, taxDetails, startDate);

    }

    public BigDecimal getXirr(HouseBuyDetails houseBuyDetails,
                              RentParameters rentDetails,
                              TaxDetails taxDetails,
                              LocalDate startDate){

        LoanDetails loanDetails = houseBuyDetails.getLoanDetails();
        long loanTermInMonths = loanDetails.getTerm() * months;
        long monthsPassed = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), LocalDate.now().withDayOfMonth(1));
        long loanPayments = Math.min(loanTermInMonths, monthsPassed);
        ArrayList<CashFlow> txns = new ArrayList<>();

        txns.add(new CashFlow(houseBuyDetails.getDownPayment().multiply(BigDecimal.valueOf(-1)), startDate));

        BigDecimal balanceLeft = loanDetails.getPrincipal();
        BigDecimal emi = getEmi(loanDetails);
        BigDecimal monthlyInterestRate = loanDetails.getInterestRate().divide(BigDecimal.valueOf(months), precision);
        BigDecimal payment = emi;
        BigDecimal initialRent = rentDetails.getInitialRent();
        BigDecimal rentAppreciationRate = rentDetails.getAnnualAppreciation();
        BigDecimal interestCap = taxDetails.getInterestCap();
        if(taxDetails.getInterestCap().equals(BigDecimal.ZERO)){
            interestCap = BigDecimal.valueOf(Double.MAX_VALUE);
        }
        int monthOfYear = 1;
        BigDecimal yearlyInterest = BigDecimal.ZERO;
        for (int i = 1; i <= loanPayments; i++) {

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

            yearlyInterest = yearlyInterest.add(interestComponent);
            monthOfYear++;

            if(monthOfYear==13){
                if(yearlyInterest.compareTo(interestCap) > 0){
                    yearlyInterest = interestCap;
                }
                BigDecimal taxSavings = yearlyInterest.multiply(taxDetails.getTaxRate());
                txns.add(new CashFlow(taxSavings, addMonths(startDate, i)));
                yearlyInterest = BigDecimal.ZERO;
                monthOfYear = 1;
            }


            int periods = i / months;
            BigDecimal rent = (initialRent.multiply((BigDecimal.valueOf(1).add(rentAppreciationRate)).pow(periods)));
            BigDecimal netIncome = rent.subtract(emi);
            txns.add(new CashFlow(netIncome, addMonths(startDate, i)));
        }

        txns.add(new CashFlow(balanceLeft.multiply(BigDecimal.valueOf(-1)), LocalDate.now()));

        BigDecimal houseFutureValue = calculateHouseValue(houseBuyDetails, Math.toIntExact(monthsPassed));

        txns.add(new CashFlow(houseFutureValue, LocalDate.now()));

        XirrResult result = XirrCalculator.calculate(txns);
        return result.getXirr();
    }

    public HouseFutureValue getFutureValue(HouseBuyDetails houseBuyDetails, TaxDetails taxDetails, BigDecimal reinvestmentRate){

        LoanDetails loanDetails = houseBuyDetails.getLoanDetails();
        int periods = loanDetails.getTerm() * months;
        BigDecimal costOfEmi = calculateCostOfEmi(houseBuyDetails, taxDetails, reinvestmentRate);
        BigDecimal houseValue = calculateHouseValue(houseBuyDetails, periods);
        BigDecimal downPaymentCost = calculateDownPaymentCost(houseBuyDetails, reinvestmentRate);
        return new HouseFutureValue(houseValue,costOfEmi,downPaymentCost);
    }

    private static BigDecimal calculateCostOfEmi(HouseBuyDetails houseBuyDetails, TaxDetails taxDetails, BigDecimal reinvestmentRate)
    {
        LoanDetails loanDetails = houseBuyDetails.getLoanDetails();
        BigDecimal emi = getEmi(loanDetails);
        BigDecimal monthlyInterestRate = loanDetails.getInterestRate().divide(BigDecimal.valueOf(months), precision);
        BigDecimal balanceLeft = loanDetails.getPrincipal();
        BigDecimal payment = emi;
        BigDecimal costOfEmi = BigDecimal.valueOf(0);
        int years = loanDetails.getTerm();
        BigDecimal interestCap = taxDetails.getInterestCap();
        if(taxDetails.getInterestCap().equals(BigDecimal.ZERO)){
            interestCap = BigDecimal.valueOf(Double.MAX_VALUE);
        }
        int monthOfYear = 1;
        BigDecimal yearlyInterest = BigDecimal.ZERO;
        for (int i = 1; i <= years * months; i++) {

            BigDecimal interestComponent = (balanceLeft.multiply(monthlyInterestRate));
            BigDecimal principalComponent = balanceLeft.min(payment.subtract(interestComponent));

            double periodInYears = ((double)(years*months - i))/months;

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


            yearlyInterest = yearlyInterest.add(interestComponent);
            monthOfYear++;

            if(monthOfYear==13){
                if(yearlyInterest.compareTo(interestCap) > 0){
                    yearlyInterest = interestCap;
                }

                BigDecimal taxSavings = yearlyInterest.multiply(taxDetails.getTaxRate());
                BigDecimal taxSavingsFutureValue = FinancialCalculator.futureValue(taxSavings,reinvestmentRate, periodInYears);
                costOfEmi = costOfEmi.subtract(taxSavingsFutureValue);
                yearlyInterest = BigDecimal.ZERO;
                monthOfYear = 1;
            }

            BigDecimal emiFutureValue = FinancialCalculator.futureValue(payment,reinvestmentRate, periodInYears);
            costOfEmi = costOfEmi.add(emiFutureValue);
        }
        return costOfEmi.add(balanceLeft);
    }

    private static BigDecimal calculateHouseValue(HouseBuyDetails houseBuyDetails, int periodInMonths){

        BigDecimal initialValue = houseBuyDetails.getDownPayment().add(houseBuyDetails.getLoanDetails().getPrincipal());
        BigDecimal rateOfAppreciation = houseBuyDetails.getHouseAppreciationRate();
        double periodInYears = (double) periodInMonths / 12;

        return FinancialCalculator.futureValue(initialValue, rateOfAppreciation, periodInYears);
    }


    private static BigDecimal calculateDownPaymentCost(HouseBuyDetails houseBuyDetails, BigDecimal reinvestmentRate){
        BigDecimal initialValue = houseBuyDetails.getDownPayment();
        int years = houseBuyDetails.getLoanDetails().getTerm();

        return FinancialCalculator.futureValue(initialValue, reinvestmentRate, years);
    }

    private static BigDecimal getEmi(LoanDetails loan){
        BigDecimal loanPrincipal = loan.getPrincipal().multiply(BigDecimal.valueOf(-1));
        BigDecimal interest = loan.getInterestRate();
        int term = loan.getTerm();
        BigDecimal interestPerPayment = interest.divide(BigDecimal.valueOf(months), precision);

        return FinancialCalculator.pmt(interestPerPayment, months * term, loanPrincipal, false);
    }
}

