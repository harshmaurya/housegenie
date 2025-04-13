package com.vanguard.housegenie.analytics;

import com.vanguard.housegenie.domain.HouseBuyDetails;
import com.vanguard.housegenie.domain.LoanDetails;
import com.vanguard.housegenie.domain.RentParameters;
import com.vanguard.housegenie.domain.ITaxDetails;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class HouseValueCalculator {

    private static final MathContext precision = MathContext.DECIMAL32;
    private static final int months = 12;

    private LocalDate addMonths(LocalDate referenceDate, long monthsToAdd) {
        return referenceDate.plusMonths(monthsToAdd);
    }

    public BigDecimal getXirr(HouseBuyDetails houseBuyDetails,
                              RentParameters rentDetails,
                              ITaxDetails taxDetails, BigDecimal inflationRate) {

        LoanDetails loanDetails = houseBuyDetails.getLoanDetails();
        int periods = loanDetails.getTerm() * months;
        LocalDate startDate = LocalDate.now().minusMonths(periods); // Simulate a past txn (bought in past)

        return getXirr(houseBuyDetails, rentDetails, taxDetails, startDate, inflationRate);

    }

    public BigDecimal getXirr(HouseBuyDetails houseBuyDetails,
                              RentParameters rentDetails,
                              ITaxDetails taxDetails,
                              LocalDate startDate, BigDecimal inflationRate) {

        LoanDetails loanDetails = houseBuyDetails.getLoanDetails();
        long loanTermInMonths = loanDetails.getTerm() * months;
        long monthsPassed = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), LocalDate.now().withDayOfMonth(1));
        long loanPayments = Math.min(loanTermInMonths, monthsPassed);
        ArrayList<CashFlow> txns = new ArrayList<>();

        // Negative Cash flow - Down payment
        txns.add(new CashFlow(houseBuyDetails.getDownPayment().multiply(BigDecimal.valueOf(-1)), startDate));

        BigDecimal balanceLeft = loanDetails.getPrincipal();
        BigDecimal emi = getEmi(loanDetails);
        BigDecimal monthlyInterestRate = loanDetails.getInterestRate().divide(BigDecimal.valueOf(months), precision);
        BigDecimal payment = emi;
        BigDecimal initialRent = rentDetails.getInitialRent();
        BigDecimal rentAppreciationRate = rentDetails.getAnnualAppreciation();
        int monthOfYear = 1;
        BigDecimal yearlyInterest = BigDecimal.ZERO;
        for (int i = 1; i <= loanPayments; i++) {

            BigDecimal interestComponent = (balanceLeft.multiply(monthlyInterestRate));
            BigDecimal principalComponent = balanceLeft.min(payment.subtract(interestComponent));
            if (balanceLeft.compareTo(emi) > 0) {
                payment = emi;
            } else {
                payment = interestComponent.add(balanceLeft);
            }

            if (payment.compareTo(BigDecimal.valueOf(0)) > 0) {
                balanceLeft = balanceLeft.subtract(principalComponent);
            } else {
                interestComponent = BigDecimal.valueOf(0);
                balanceLeft = BigDecimal.valueOf(0);
            }

            yearlyInterest = yearlyInterest.add(interestComponent);
            monthOfYear++;
            int periods = i / months;
            BigDecimal rent = (initialRent.multiply((BigDecimal.valueOf(1).add(rentAppreciationRate)).pow(periods)));
            BigDecimal netIncome = rent.subtract(emi);

            // Cash flow - Rent & EMI
            txns.add(new CashFlow(netIncome, addMonths(startDate, i)));

            if (monthOfYear == 13) {
                BigDecimal taxSavings = getTaxSavings(houseBuyDetails, taxDetails, inflationRate,
                        periods, yearlyInterest, rent);

                // Positive Cash flow - Tax Savings
                txns.add(new CashFlow(taxSavings, addMonths(startDate, i)));

                BigDecimal annualMaintenance = houseBuyDetails.getAnnualMaintenance();
                BigDecimal maintenanceCostCurrent = FinancialCalculator.futureValue(annualMaintenance, inflationRate, periods);

                // Negative Cash flow - Maintenance cost
                txns.add(new CashFlow(maintenanceCostCurrent.multiply(BigDecimal.valueOf(-1)), addMonths(startDate, i)));
                yearlyInterest = BigDecimal.ZERO;
                monthOfYear = 1;
            }
        }

        // Negative Cash flow - Final EMI payment
        txns.add(new CashFlow(balanceLeft.multiply(BigDecimal.valueOf(-1)), LocalDate.now()));

        BigDecimal houseFutureValue = calculateHouseValue(houseBuyDetails,
                Math.toIntExact(monthsPassed), inflationRate);

        // Positive Cash flow - House sell value
        txns.add(new CashFlow(houseFutureValue, LocalDate.now()));

        XirrResult result = XirrCalculator.calculate(txns);
        return result.getXirr();
    }

    public HouseFutureValue getFutureValue(HouseBuyDetails houseBuyDetails, ITaxDetails taxDetails, BigDecimal reinvestmentRate) {

        LoanDetails loanDetails = houseBuyDetails.getLoanDetails();
        int periods = loanDetails.getTerm() * months;
        BigDecimal costOfEmi = calculateCostOfEmi(houseBuyDetails, taxDetails, reinvestmentRate);
        BigDecimal houseValue = calculateHouseValue(houseBuyDetails, periods, reinvestmentRate);
        BigDecimal costOfMaintenance = calculateCostOfMaintenance(houseBuyDetails, periods, reinvestmentRate);
        BigDecimal downPaymentCost = calculateDownPaymentCost(houseBuyDetails, reinvestmentRate);
        return new HouseFutureValue(houseValue, costOfEmi, downPaymentCost, costOfMaintenance);
    }

    private static BigDecimal calculateCostOfEmi(HouseBuyDetails houseBuyDetails, ITaxDetails taxDetails, BigDecimal reinvestmentRate) {
        LoanDetails loanDetails = houseBuyDetails.getLoanDetails();
        BigDecimal emi = getEmi(loanDetails);
        BigDecimal monthlyInterestRate = loanDetails.getInterestRate().divide(BigDecimal.valueOf(months), precision);
        BigDecimal balanceLeft = loanDetails.getPrincipal();
        BigDecimal payment = emi;
        BigDecimal costOfEmi = BigDecimal.valueOf(0);
        int years = loanDetails.getTerm();
        int monthOfYear = 1;
        BigDecimal yearlyInterest = BigDecimal.ZERO;
        for (int i = 1; i <= years * months; i++) {

            BigDecimal interestComponent = (balanceLeft.multiply(monthlyInterestRate));
            BigDecimal principalComponent = balanceLeft.min(payment.subtract(interestComponent));

            double periodInYears = ((double) (years * months - i)) / months;

            if (balanceLeft.compareTo(emi) > 0) {
                payment = emi;
            } else {
                payment = interestComponent.add(balanceLeft);
            }

            if (payment.compareTo(BigDecimal.valueOf(0)) > 0) {
                balanceLeft = balanceLeft.subtract(principalComponent);
            } else {
                interestComponent = BigDecimal.valueOf(0);
                balanceLeft = BigDecimal.valueOf(0);
            }


            yearlyInterest = yearlyInterest.add(interestComponent);
            monthOfYear++;

            BigDecimal rent = BigDecimal.ZERO; // own house scenario

            if (monthOfYear == 13) {
                BigDecimal taxSavings = getTaxSavings(houseBuyDetails, taxDetails, reinvestmentRate,
                        periodInYears, yearlyInterest, rent);
                BigDecimal taxSavingsFutureValue = FinancialCalculator.futureValue(taxSavings, reinvestmentRate, periodInYears);
                System.out.println("tax savings:" + taxSavingsFutureValue);
                costOfEmi = costOfEmi.subtract(taxSavingsFutureValue);
                yearlyInterest = BigDecimal.ZERO;
                monthOfYear = 1;
            }

            BigDecimal emiFutureValue = FinancialCalculator.futureValue(payment, reinvestmentRate, periodInYears);
            costOfEmi = costOfEmi.add(emiFutureValue);
        }
        return costOfEmi.add(balanceLeft);
    }

    private static BigDecimal getTaxSavings(HouseBuyDetails houseBuyDetails, ITaxDetails taxDetails,
                                            BigDecimal reinvestmentRate, double periodInYears, BigDecimal yearlyInterest, BigDecimal rent) {
        BigDecimal annualMaintenance = houseBuyDetails.getAnnualMaintenance();
        BigDecimal maintenanceCost = FinancialCalculator.futureValue(annualMaintenance, reinvestmentRate, periodInYears);
        return taxDetails.calculateTaxSavings(yearlyInterest, maintenanceCost, rent);
    }

    private static BigDecimal calculateHouseValue(HouseBuyDetails houseBuyDetails, int periodInMonths,
                                                  BigDecimal reinvestmentRate) {

        BigDecimal initialValue = houseBuyDetails.getDownPayment().add(houseBuyDetails.getLoanDetails().getPrincipal());
        BigDecimal rateOfAppreciation = houseBuyDetails.getHouseAppreciationRate();
        double periodInYears = (double) periodInMonths / 12;
        return FinancialCalculator.futureValue(initialValue, rateOfAppreciation, periodInYears);
    }

    private static BigDecimal calculateCostOfMaintenance(HouseBuyDetails houseBuyDetails, int periodInMonths,
                                                  BigDecimal reinvestmentRate) {
        double periodInYears = (double) periodInMonths / 12;
        BigDecimal totalCostOfMaintenance = BigDecimal.ZERO;
        BigDecimal annualMaintenance = houseBuyDetails.getAnnualMaintenance();
        for (int i = 1; i <= periodInYears; i++) {
            BigDecimal maintenanceCostInflationAdjusted = FinancialCalculator.futureValue(annualMaintenance, reinvestmentRate, i - 1);
            BigDecimal futureValueOfMaintenance = FinancialCalculator.futureValue(maintenanceCostInflationAdjusted,
                    reinvestmentRate, periodInYears - i + 1);
            totalCostOfMaintenance = totalCostOfMaintenance.add(futureValueOfMaintenance);
        }
        return totalCostOfMaintenance;
    }


    private static BigDecimal calculateDownPaymentCost(HouseBuyDetails houseBuyDetails, BigDecimal reinvestmentRate) {
        BigDecimal initialValue = houseBuyDetails.getDownPayment();
        int years = houseBuyDetails.getLoanDetails().getTerm();

        return FinancialCalculator.futureValue(initialValue, reinvestmentRate, years);
    }

    private static BigDecimal getEmi(LoanDetails loan) {
        BigDecimal loanPrincipal = loan.getPrincipal().multiply(BigDecimal.valueOf(-1));
        BigDecimal interest = loan.getInterestRate();
        int term = loan.getTerm();
        BigDecimal interestPerPayment = interest.divide(BigDecimal.valueOf(months), precision);

        return FinancialCalculator.pmt(interestPerPayment, months * term, loanPrincipal, false);
    }
}

