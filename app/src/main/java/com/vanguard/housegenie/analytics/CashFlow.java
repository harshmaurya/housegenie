package com.vanguard.housegenie.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class CashFlow {
    private final BigDecimal amount;
    private final LocalDate date;

    public CashFlow(BigDecimal amount, LocalDate date) {
        this.amount = amount;
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
