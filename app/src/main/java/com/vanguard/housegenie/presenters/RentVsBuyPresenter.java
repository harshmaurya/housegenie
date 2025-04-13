package com.vanguard.housegenie.presenters;

import com.vanguard.housegenie.contracts.HouseVsOtherInvestmentContract;
import com.vanguard.housegenie.contracts.RentVsRentvestingContract;
import com.vanguard.housegenie.contracts.RentvsBuyContract;
import com.vanguard.housegenie.domain.RentVsBuyArgs;
import com.vanguard.housegenie.domain.RentVsBuyResult;
import com.vanguard.housegenie.domain.RentVsRentvestingArgs;
import com.vanguard.housegenie.models.RentVsBuyModel;

public class RentVsBuyPresenter implements RentvsBuyContract.Presenter {

    private final RentvsBuyContract.View view;
    private final RentVsBuyModel model;

    public RentVsBuyPresenter(RentvsBuyContract.View view){
        this.view = view;
        model = new RentVsBuyModel();
    }

    @Override
    public void calculate(RentVsBuyArgs args) {
        RentVsBuyResult result = model.CompareBuyRent(args);
        view.showResult(result);
    }
}

