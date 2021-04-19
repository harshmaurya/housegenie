package com.vanguard.housegenie.analytics;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class HouseFutureValue implements Parcelable {

    private final BigDecimal houseValue;
    private final BigDecimal costOfEmi;
    private final BigDecimal costOfDownPayment;

    public HouseFutureValue(BigDecimal houseValue, BigDecimal costOfEmi, BigDecimal costOfDownPayment){

        this.houseValue = houseValue;
        this.costOfEmi = costOfEmi;
        this.costOfDownPayment = costOfDownPayment;
    }

    protected HouseFutureValue(Parcel in) {
        this.houseValue = (BigDecimal) in.readSerializable();
        this.costOfEmi = (BigDecimal) in.readSerializable();;
        this.costOfDownPayment = (BigDecimal) in.readSerializable();;
    }

    public static final Creator<HouseFutureValue> CREATOR = new Creator<HouseFutureValue>() {
        @Override
        public HouseFutureValue createFromParcel(Parcel in) {
            return new HouseFutureValue(in);
        }

        @Override
        public HouseFutureValue[] newArray(int size) {
            return new HouseFutureValue[size];
        }
    };

    public BigDecimal getCostOfDownPayment() {
        return costOfDownPayment;
    }

    public BigDecimal getCostOfEmi() {
        return costOfEmi;
    }

    public BigDecimal getHouseValue() {
        return houseValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(houseValue);
        dest.writeSerializable(costOfEmi);
        dest.writeSerializable(costOfDownPayment);
    }
}
