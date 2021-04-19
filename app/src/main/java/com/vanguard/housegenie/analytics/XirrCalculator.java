package com.vanguard.housegenie.analytics;

import org.decampo.xirr.Transaction;
import org.decampo.xirr.Xirr;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XirrCalculator {

    public static XirrResult calculate(List<CashFlow> cashFlows){

        if(cashFlows.stream().allMatch(cashFlow -> cashFlow.getAmount().compareTo(BigDecimal.valueOf(0)) < 0)){
            return new XirrResult(BigDecimal.ZERO, false, "All cash flows cannot be negative");
        }
        if(cashFlows.stream().allMatch(cashFlow -> cashFlow.getAmount().compareTo(BigDecimal.valueOf(0)) > 0)){
            return new XirrResult(BigDecimal.ZERO, false, "All cash flows cannot be positive");
        }
        if(cashFlows.size() < 2){
            return new XirrResult(BigDecimal.ZERO, false, "At least 2 cashflows are required");
        }

        try
        {
            BigDecimal rate = primaryMethod(cashFlows);
            return new XirrResult(rate, true, "");
        }
        catch (Exception ignored){

        }
        try {
            BigDecimal rate = secondaryMethod(cashFlows);
            return new XirrResult(rate, true, "");
        }
        catch (Exception e){
            return new XirrResult(BigDecimal.ZERO, false, "");
        }
    }

    private static  BigDecimal primaryMethod(List<CashFlow> cashFlows){
        double initialGuess = 10;
        List<Transaction> transactions = cashFlows.stream()
                .map(cashFlow -> new Transaction(cashFlow.getAmount().doubleValue(), cashFlow.getDate()))
                .collect(Collectors.toList());
        double xirr = Xirr.builder().withGuess(initialGuess).withTransactions(transactions).xirr();
        return BigDecimal.valueOf(xirr*100);
    }

    @Nullable
    private static BigDecimal secondaryMethod(List<CashFlow> cashFlows) {
        List<Date> dates = new ArrayList<>();
        List<BigDecimal> amounts = new ArrayList<>();
        for (CashFlow cashflow: cashFlows) {
            dates.add(cashflow.getDate());
            amounts.add(cashflow.getAmount());
        }
        Map<String, Object> xirrMap = LocalXirr.Xirr(dates, amounts);
        BigDecimal rate = (BigDecimal) xirrMap.getOrDefault("rate", BigDecimal.ZERO);
        return rate.multiply(BigDecimal.valueOf(100));
    }
}
