package com.vanguard.housegenie.analytics;

import java.math.BigDecimal;
import java.util.Date;

public class CashFlow {
    private final BigDecimal amount;
    private final Date date;

    public CashFlow(BigDecimal amount, Date date) {
        this.amount = amount;
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }
}
