package com.vanguard.housegenie.domain;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class RentvsRentvestingResult implements Parcelable {

    protected RentvsRentvestingResult(Parcel in) {
    }

    public static final Creator<RentvsRentvestingResult> CREATOR = new Creator<RentvsRentvestingResult>() {
        @Override
        public RentvsRentvestingResult createFromParcel(Parcel in) {
            return new RentvsRentvestingResult(in);
        }

        @Override
        public RentvsRentvestingResult[] newArray(int size) {
            return new RentvsRentvestingResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }
}
