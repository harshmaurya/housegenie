package com.vanguard.housegenie.analytics;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class LocalXirr {
    private static int z = 0;
    private static int stepLimit = 0;
    private static BigDecimal step = BigDecimal.valueOf(0.1);
    private static BigDecimal d = BigDecimal.valueOf(0.5);
    private static Date L_MaxDate = new Date();
    private static Date L_MinDate = new Date();
    private static BigDecimal tempAmont = null;
    private static MathContext mathContext = new MathContext(18);

    public static Map<String, Object> Xirr(List<Date> dates, List<BigDecimal> amounts) {
        L_MaxDate = dates.get(0);
        L_MinDate = dates.get(0);
        Map<String, Object> r_map = new HashMap<String, Object>();
        for (Date date : dates) {
            if (date.getTime() > L_MaxDate.getTime()) {
                L_MaxDate = date;
            }
            if (date.getTime() < L_MinDate.getTime()) {
                L_MinDate = date;
            }
        }
        while (true) {
            tempAmont = amounts.get(0);
            for (int i = 1; i < amounts.size(); i++) {
                tempAmont = tempAmont
                        .add(amounts
                                .get(i).divide(
                                        BigDecimal.valueOf(Math.pow(d.add(BigDecimal.valueOf(1)).doubleValue(),
                                                BigDecimal
                                                        .valueOf((dates.get(i).getTime() - dates.get(0).getTime())
                                                                / (24 * 60 * 60 * 1000))
                                                        .divide(BigDecimal.valueOf(365), mathContext).doubleValue())),
                                        mathContext));
            }
            if (tempAmont.compareTo(BigDecimal.ZERO) > 0 && z == 0) {
                step = step.divide(BigDecimal.valueOf(2), mathContext);
                z = 1;
            }
            if (tempAmont.compareTo(BigDecimal.ZERO) < 0 && z == 1) {
                step = step.divide(BigDecimal.valueOf(2), mathContext);
                z = 0;
            }
            if (z == 0) {
                d = d.subtract(step);
            } else {
                d = d.add(step);
            }
            stepLimit = stepLimit + 1;
            if (stepLimit == 10000) {
                r_map.put("reCode", false);
                return r_map;
            }
            if (Math.round(tempAmont.doubleValue()) == 0) {
                break;
            }
        }
        r_map.put("reCode", false);
        r_map.put("rate", d);
        return r_map;
    }

}