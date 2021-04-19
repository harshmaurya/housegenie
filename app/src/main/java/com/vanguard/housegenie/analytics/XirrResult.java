package com.vanguard.housegenie.analytics;

import java.math.BigDecimal;

public class XirrResult {
    private final BigDecimal xirr;
    private final Boolean valid;
    private final String message;

    public XirrResult(BigDecimal xirr, Boolean valid, String message) {
        this.xirr = xirr;
        this.valid = valid;
        this.message = message;
    }

    public BigDecimal getXirr() {
        return xirr;
    }

    public Boolean getValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}
