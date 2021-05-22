package com.vanguard.housegenie.contracts;

import com.vanguard.housegenie.domain.HouseValueArgs;
import com.vanguard.housegenie.domain.HouseValueResult;
import com.vanguard.housegenie.domain.HouseVsOtherInvestmentArgs;
import com.vanguard.housegenie.domain.HouseVsOtherInvestmentResult;

public interface HouseValueContract {
    interface View {
        void showResult(HouseValueResult result);
    }

    interface Presenter {
        void calculate(HouseValueArgs args);
    }
}
