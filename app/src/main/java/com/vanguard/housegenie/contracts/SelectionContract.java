package com.vanguard.housegenie.contracts;

public interface SelectionContract {

    interface View{
        void navigateToBuyVsRent();
    }

    interface Presenter{
        void onRentvsBuyClick();
    }
}

