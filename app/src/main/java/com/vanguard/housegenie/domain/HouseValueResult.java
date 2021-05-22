package com.vanguard.housegenie.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class HouseValueResult implements Parcelable {

    private final BigDecimal houseXirr;
    private final int termInMonths;
    private final BigDecimal investedAmount;
    private final BigDecimal returnFromHouse;

    public HouseValueResult(BigDecimal houseXirr, int termInMonths,
                                        BigDecimal investedAmount, BigDecimal returnFromHouse) {
        this.houseXirr = houseXirr;
        this.termInMonths = termInMonths;
        this.investedAmount = investedAmount;
        this.returnFromHouse = returnFromHouse;
    }

    protected HouseValueResult(Parcel in) {
        this.houseXirr = (BigDecimal) in.readSerializable();
        this.termInMonths = in.readInt();
        this.investedAmount = (BigDecimal) in.readSerializable();
        this.returnFromHouse = (BigDecimal) in.readSerializable();
    }


    public static final Parcelable.Creator<HouseVsOtherInvestmentResult> CREATOR = new Parcelable.Creator<HouseVsOtherInvestmentResult>() {
        @Override
        public HouseVsOtherInvestmentResult createFromParcel(Parcel in) {
            return new HouseVsOtherInvestmentResult(in);
        }

        @Override
        public HouseVsOtherInvestmentResult[] newArray(int size) {
            return new HouseVsOtherInvestmentResult[size];
        }
    };

    public int getTermInMonths() {
        return termInMonths;
    }

    public BigDecimal getHouseXirr() {
        return houseXirr;
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public BigDecimal getReturnFromHouse() {
        return returnFromHouse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(houseXirr);
        dest.writeInt(termInMonths);
        dest.writeSerializable(investedAmount);
        dest.writeSerializable(returnFromHouse);
    }
}
