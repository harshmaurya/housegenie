package com.vanguard.housegenie.presenters;

import com.vanguard.housegenie.contracts.HouseVsOtherInvestmentContract;
import com.vanguard.housegenie.domain.HouseVsOtherInvestmentArgs;
import com.vanguard.housegenie.domain.HouseVsOtherInvestmentResult;
import com.vanguard.housegenie.models.HouseVsOtherInvestmentModel;
import com.vanguard.housegenie.models.RentVsBuyModel;

public class HouseVsOtherInvestmentPresenter implements HouseVsOtherInvestmentContract.Presenter {

    private final HouseVsOtherInvestmentContract.View view;
    private final HouseVsOtherInvestmentModel model;

    public HouseVsOtherInvestmentPresenter(HouseVsOtherInvestmentContract.View view) {
        this.view = view;
        model = new HouseVsOtherInvestmentModel();
    }

    @Override
    public void calculate(HouseVsOtherInvestmentArgs args) {
        HouseVsOtherInvestmentResult result = model.CompareInvestments(args);
        view.showResult(result);
    }
}
