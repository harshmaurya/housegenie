package com.vanguard.housegenie.domain;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.vanguard.housegenie.analytics.HouseFutureValue;

import java.math.BigDecimal;

public class RentVsBuyResult implements Parcelable {

    private final BigDecimal rentValue;
    private final int term;
    private final HouseFutureValue houseFutureValue;

    public RentVsBuyResult(HouseFutureValue houseFutureValue, BigDecimal rentValue, int term){
        this.houseFutureValue = houseFutureValue;
        this.rentValue = rentValue;
        this.term = term;
    }

    protected RentVsBuyResult(Parcel in) {
        this.houseFutureValue = in.readParcelable(HouseFutureValue.class.getClassLoader());
        this.rentValue = (BigDecimal) in.readSerializable();
        this.term = in.readInt();
    }

    public static final Creator<RentVsBuyResult> CREATOR = new Creator<RentVsBuyResult>() {
        @Override
        public RentVsBuyResult createFromParcel(Parcel in) {
            return new RentVsBuyResult(in);
        }

        @Override
        public RentVsBuyResult[] newArray(int size) {
            return new RentVsBuyResult[size];
        }
    };

    public BigDecimal getRentValue(){
        return this.rentValue;
    }

    public HouseFutureValue getHouseFutureValue() {
        return houseFutureValue;
    }

    public int getTerm() {
        return term;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(houseFutureValue, flags);
        dest.writeSerializable(rentValue);
        dest.writeInt(term);
    }
}
