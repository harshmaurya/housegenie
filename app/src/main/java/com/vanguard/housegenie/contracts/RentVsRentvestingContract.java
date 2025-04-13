package com.vanguard.housegenie.contracts;

import com.vanguard.housegenie.domain.RentVsRentvestingArgs;
import com.vanguard.housegenie.domain.RentvsRentvestingResult;

public interface RentVsRentvestingContract {
    interface View {
        void showResult(RentvsRentvestingResult result);
    }

    interface Presenter {
        void calculate(RentVsRentvestingArgs args);
    }
}
