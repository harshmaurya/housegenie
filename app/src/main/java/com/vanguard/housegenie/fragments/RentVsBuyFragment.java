package com.vanguard.housegenie.fragments;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import com.vanguard.housegenie.R;
import com.vanguard.housegenie.contracts.RentvsBuyContract;
import com.vanguard.housegenie.domain.*;
import com.vanguard.housegenie.presenters.RentVsBuyPresenter;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import com.vanguard.housegenie.utils.*;

import static com.vanguard.housegenie.analytics.FinancialCalculator.toFractionalRate;

public class RentVsBuyFragment extends Fragment implements RentvsBuyContract.View {

    private final RentVsBuyPresenter presenter;
    private final int defaultTerm = 10;

    public RentVsBuyFragment() {
        presenter = new RentVsBuyPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rent_vs_buy, container, false);
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
                    TaxDetails taxDetails = taxDetailsFetcher.getTaxDetails();
                    BigDecimal inflationRate = getReInvestmentDetails(view);
                    if(purchaseDetails==null || rentParameters==null || taxDetails ==null){
                        showErrorMessage();
                        return;
                    }
                    HouseBuyDetails houseValueDetails = new HouseBuyDetails(purchaseDetails.getDownPayment(), purchaseDetails.getLoanDetails()
                            ,getHouseAppreciation(view));
                    presenter.calculate(new RentVsBuyArgs(houseValueDetails, rentParameters, taxDetails, inflationRate));
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
    public void showResult(RentVsBuyResult result) {
        final NavController navController = NavHostFragment.findNavController(this);
        final NavDirections action = RentVsBuyFragmentDirections.actionRentVsBuyFragmentToBuyVsRentResultFragment(result);
        navController.navigate(action);
    }
}