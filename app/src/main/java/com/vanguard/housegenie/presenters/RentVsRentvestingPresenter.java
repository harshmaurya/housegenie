package com.vanguard.housegenie.presenters;

import com.vanguard.housegenie.contracts.RentVsRentvestingContract;
import com.vanguard.housegenie.domain.RentVsRentvestingArgs;
import com.vanguard.housegenie.models.RentVsBuyModel;

public class RentVsRentvestingPresenter implements RentVsRentvestingContract.Presenter {

    private final RentVsRentvestingContract.View view;
    private final RentVsBuyModel model;

    public RentVsRentvestingPresenter(RentVsRentvestingContract.View view){
        this.view = view;
        model = new RentVsBuyModel();
    }

    @Override
    public void calculate(RentVsRentvestingArgs args) {
        //RentVsBuyResult result = model.CompareBuyRent(args);
        //view.showResult(result);
    }
}
