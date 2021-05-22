package com.vanguard.housegenie.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.vanguard.housegenie.R;
import com.vanguard.housegenie.contracts.HouseVsOtherInvestmentContract;
import com.vanguard.housegenie.domain.*;
import com.vanguard.housegenie.presenters.HouseVsOtherInvestmentPresenter;
import com.vanguard.housegenie.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;

import static com.vanguard.housegenie.analytics.FinancialCalculator.toFractionalRate;

public class HouseVsOtherInvestmentFragment extends Fragment implements HouseVsOtherInvestmentContract.View {

    private final HouseVsOtherInvestmentPresenter presenter;
    private String investmentTypeSelection;

    private final HashMap<String, BigDecimal> investmentReturns = new HashMap<>();
    private final int defaultTerm =10;

    public HouseVsOtherInvestmentFragment() {
        presenter = new HouseVsOtherInvestmentPresenter(this);
    }

    private void PopulateData() {
        investmentReturns.put(getStringResource(R.string.savings_account), BigDecimal.valueOf(4));
        investmentReturns.put(getStringResource(R.string.fixed_deposit), BigDecimal.valueOf(6));
        investmentReturns.put(getStringResource(R.string.equity_fund), BigDecimal.valueOf(10));
        investmentReturns.put(getStringResource(R.string.debt_fund), BigDecimal.valueOf(7));
        investmentReturns.put(getStringResource(R.string.others), BigDecimal.valueOf(0));
    }

    private String getStringResource(int id){
        return getResources().getString(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_vs_other_investment, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {

        TaxDetailsFetcher taxDetailsFetcher = TaxDetailFragment.SetupTaxView(view, getContext());
        PurchaseDetailsFetcher purchaseDetailsFetcher = PurchaseDetailsFragment.SetupPurchaseView(view, getContext());
        PopulateData();
        SetupInvestmentSelection(view);
        Button calculate = view.findViewById(R.id.btnCalculate);
        calculate.setOnClickListener(view1 ->
        {
            RentParameters rentParameters = getRentParameters(view);
            PurchaseDetails purchaseDetails = purchaseDetailsFetcher.getPurchaseDetails();
            TaxDetails taxDetails = taxDetailsFetcher.getTaxDetails();
            BigDecimal investmentReturn = getInvestmentDetails(view);
            if(purchaseDetails==null || rentParameters==null || taxDetails ==null){
                showErrorMessage();
                return;
            }
            HouseBuyDetails houseValueDetails = new HouseBuyDetails(purchaseDetails.getDownPayment(), purchaseDetails.getLoanDetails()
                    ,getHouseAppreciation(view));
            HouseVsOtherInvestmentArgs args = new HouseVsOtherInvestmentArgs(houseValueDetails, rentParameters, taxDetails, investmentReturn);
            presenter.calculate(args);
        });
    }


    private BigDecimal getInvestmentDetails(@NotNull View view){

        EditText txtAvgInvestmentReturn = view.findViewById(R.id.txtInvestmentRate);
        String avgInvestmentRateString = txtAvgInvestmentReturn.getText().toString();
        if(StringUtils.isNullOrEmpty(avgInvestmentRateString)){
            return null;
        }
        BigDecimal avgInvestmentReturn = new BigDecimal(avgInvestmentRateString);
        return toFractionalRate(avgInvestmentReturn);
    }

    private void SetupInvestmentSelection(@NotNull View view) {
        Spinner spinner = view.findViewById(R.id.investment_type);
        TextInputLayout txtInvestmentName = view.findViewById(R.id.txtInvestmentNameHost);
        EditText txtInvestmentRate = view.findViewById(R.id.txtInvestmentRate);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                investmentTypeSelection = (String) parent.getItemAtPosition(position);
                String rateOfReturn = investmentReturns
                        .getOrDefault(investmentTypeSelection, BigDecimal.valueOf(0)).toString();
                txtInvestmentRate.setText(rateOfReturn);

                if(getStringResource(R.string.others).equals(investmentTypeSelection)){
                    txtInvestmentName.setVisibility(View.VISIBLE);
                }
                else {
                    txtInvestmentName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.investment_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void showErrorMessage() {
        Toast.makeText(this.getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
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
    public void showResult(HouseVsOtherInvestmentResult result) {
        final NavController navController = NavHostFragment.findNavController(this);
        final NavDirections action = HouseVsOtherInvestmentFragmentDirections
                .actionHouseVsOtherInvestmentFragmentToHouseVsInvestmentResultFragment(result);
        navController.navigate(action);
    }
}
