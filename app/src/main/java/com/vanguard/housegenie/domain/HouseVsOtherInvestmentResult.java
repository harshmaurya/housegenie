package com.vanguard.housegenie.domain;

import android.os.Parcel;
import android.os.Parcelable;
import com.vanguard.housegenie.analytics.HouseFutureValue;

import java.math.BigDecimal;

public class HouseVsOtherInvestmentResult implements Parcelable {

    private final BigDecimal houseXirr;
    private final BigDecimal investmentXirr;
    private final int term;
    private final BigDecimal investedAmount;
    private final BigDecimal returnFromHouse;
    private final BigDecimal returnFromInvestment;

    public HouseVsOtherInvestmentResult(BigDecimal houseXirr, BigDecimal investmentXirr, int term,
                                        BigDecimal investedAmount, BigDecimal returnFromHouse,
                                        BigDecimal returnFromInvestment) {

        this.houseXirr = houseXirr;
        this.investmentXirr = investmentXirr;
        this.term = term;
        this.investedAmount = investedAmount;
        this.returnFromHouse = returnFromHouse;
        this.returnFromInvestment = returnFromInvestment;
    }

    protected HouseVsOtherInvestmentResult(Parcel in) {
        this.houseXirr = (BigDecimal) in.readSerializable();
        this.investmentXirr = (BigDecimal) in.readSerializable();
        this.term = in.readInt();
        this.investedAmount = (BigDecimal) in.readSerializable();
        this.returnFromHouse = (BigDecimal) in.readSerializable();
        this.returnFromInvestment = (BigDecimal) in.readSerializable();
    }


    public static final Creator<HouseVsOtherInvestmentResult> CREATOR = new Creator<HouseVsOtherInvestmentResult>() {
        @Override
        public HouseVsOtherInvestmentResult createFromParcel(Parcel in) {
            return new HouseVsOtherInvestmentResult(in);
        }

        @Override
        public HouseVsOtherInvestmentResult[] newArray(int size) {
            return new HouseVsOtherInvestmentResult[size];
        }
    };

    public int getTerm() {
        return term;
    }

    public BigDecimal getHouseXirr() {
        return houseXirr;
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public BigDecimal getInvestmentXirr() {
        return investmentXirr;
    }

    public BigDecimal getReturnFromHouse() {
        return returnFromHouse;
    }

    public BigDecimal getReturnFromInvestment() {
        return returnFromInvestment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(houseXirr);
        dest.writeSerializable(investmentXirr);
        dest.writeInt(term);
        dest.writeSerializable(investedAmount);
        dest.writeSerializable(returnFromHouse);
        dest.writeSerializable(returnFromInvestment);
    }
}
