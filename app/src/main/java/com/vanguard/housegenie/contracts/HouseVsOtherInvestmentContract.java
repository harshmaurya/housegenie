package com.vanguard.housegenie.contracts;

import com.vanguard.housegenie.domain.HouseVsOtherInvestmentArgs;
import com.vanguard.housegenie.domain.HouseVsOtherInvestmentResult;

public interface HouseVsOtherInvestmentContract {
    interface View {
        void showResult(HouseVsOtherInvestmentResult result);
    }

    interface Presenter {
        void calculate(HouseVsOtherInvestmentArgs args);
    }
}

