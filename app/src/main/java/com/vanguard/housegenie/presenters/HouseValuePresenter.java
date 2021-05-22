package com.vanguard.housegenie.presenters;

import com.vanguard.housegenie.contracts.HouseValueContract;
import com.vanguard.housegenie.domain.HouseValueArgs;
import com.vanguard.housegenie.domain.HouseValueResult;
import com.vanguard.housegenie.domain.HouseVsOtherInvestmentResult;
import com.vanguard.housegenie.models.HouseValueModel;
import com.vanguard.housegenie.models.HouseVsOtherInvestmentModel;

public class HouseValuePresenter implements HouseValueContract.Presenter {

    private final HouseValueContract.View view;
    private final HouseValueModel model;

    public HouseValuePresenter(HouseValueContract.View view) {
        this.view = view;
        model = new HouseValueModel();
    }

    @Override
    public void calculate(HouseValueArgs args) {
        HouseValueResult result = model.CalculateHouseValue(args);
        view.showResult(result);
    }
}
