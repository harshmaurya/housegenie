package com.vanguard.housegenie.contracts;

import com.vanguard.housegenie.domain.RentVsBuyArgs;
import com.vanguard.housegenie.domain.RentVsBuyResult;

public interface RentvsBuyContract {

    interface View{
        void showResult(RentVsBuyResult result);
    }

    interface Presenter{
        void calculate(RentVsBuyArgs args);
    }
}

