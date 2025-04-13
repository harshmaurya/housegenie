package com.vanguard.housegenie.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.vanguard.housegenie.R;
import com.vanguard.housegenie.contracts.RentVsRentvestingContract;
import com.vanguard.housegenie.domain.*;
import com.vanguard.housegenie.presenters.RentVsRentvestingPresenter;
import com.vanguard.housegenie.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import static com.vanguard.housegenie.analytics.FinancialCalculator.toFractionalRate;

public class RentVsRentvestingFragment extends Fragment implements RentVsRentvestingContract.View {
    private final RentVsRentvestingPresenter presenter;
    private final int defaultTerm = 10;

    public RentVsRentvestingFragment() {
        presenter = new RentVsRentvestingPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rent_vs_rentvesting, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {

        TaxDetailsFetcher taxDetailsFetcher = TaxDetailFragment.SetupTaxView(view, getContext());
        PurchaseDetailsFetcher purchaseDetailsFetcher = PurchaseDetailsFragment.SetupPurchaseView(view, getContext());
        Button rentvsBuy = view.findViewById(R.id.btnCalculate);
        rentvsBuy.setOnClickListener(view1 ->
        {
            PurchaseDetails purchaseDetails = purchaseDetailsFetcher.getPurchaseDetails();
            RentParameters rentParameters = getRentParameters(view);
            ITaxDetails taxDetails = taxDetailsFetcher.getTaxDetails();
            BigDecimal inflationRate = getReInvestmentDetails(view);
            if(purchaseDetails==null || rentParameters==null || taxDetails ==null){
                showErrorMessage();
                return;
            }
            HouseBuyDetails houseValueDetails = new HouseBuyDetails(purchaseDetails.getDownPayment(), purchaseDetails.getLoanDetails()
                    ,getHouseAppreciation(view), purchaseDetails.getMaintenance());
            presenter.calculate(new RentVsRentvestingArgs());
        });
    }

    private void showErrorMessage() {
        Toast.makeText(this.getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
    }

    private BigDecimal getReInvestmentDetails(@NotNull View view){

        EditText txtAvgInvestmentReturn = view.findViewById(R.id.txtInvestmentRate);
        String avgInvestmentRateString = txtAvgInvestmentReturn.getText().toString();
        if(StringUtils.isNullOrEmpty(avgInvestmentRateString)){
            return null;
        }
        BigDecimal avgInvestmentReturn = new BigDecimal(avgInvestmentRateString);
        return toFractionalRate(avgInvestmentReturn);
    }


    private RentParameters getRentParameters(@NotNull View view) {
        EditText txtRent = view.findViewById(R.id.txtRentAmount);
        EditText txtRentAppreciation = view.findViewById(R.id.txtRentAppreciation);

        String rentString = txtRent.getText().toString();
        String rentAppreciationString = txtRentAppreciation.getText().toString();


        if(StringUtils.isNullOrEmpty(rentString) || StringUtils.isNullOrEmpty(rentAppreciationString)){
            return null;
        }

        BigDecimal rent = new BigDecimal(rentString);
        BigDecimal rentAppreciation = new BigDecimal(rentAppreciationString);

        return new RentParameters(rent,toFractionalRate(rentAppreciation));
    }

    private BigDecimal getHouseAppreciation(@NotNull View view) {
        EditText houseAppreciation = view.findViewById(R.id.txtPropertyAppreciation);
        String appreciationString = houseAppreciation.getText().toString();
        BigDecimal appreciation = new BigDecimal(appreciationString);
        return toFractionalRate(appreciation);
    }


    @Override
    public void showResult(RentvsRentvestingResult result) {
        
    }
}
