package com.vanguard.housegenie.models;
import com.vanguard.housegenie.analytics.HouseFutureValue;
import com.vanguard.housegenie.analytics.HouseValueCalculator;
import com.vanguard.housegenie.analytics.RentValueCalculator;
import com.vanguard.housegenie.domain.*;
import java.math.BigDecimal;

public class RentVsBuyModel {

    private final HouseValueCalculator houseValueCalculator;
    private final RentValueCalculator rentValueCalculator;

    public RentVsBuyModel(){
        this.houseValueCalculator = new HouseValueCalculator();
        this.rentValueCalculator = new RentValueCalculator();
    }

    public RentVsBuyResult CompareBuyRent(RentVsBuyArgs args){

        int term = args.getHouseBuyDetails().getLoanDetails().getTerm();
        HouseFutureValue result = houseValueCalculator.getFutureValue(args.getHouseBuyDetails(), args.getTaxDetails(), args.getInflationRate());

        BigDecimal rentFutureValue = rentValueCalculator
                .getRentFutureValue(args.getRentParameters(), term, args.getInflationRate());

        return new RentVsBuyResult(result, rentFutureValue, term);
    }
}

