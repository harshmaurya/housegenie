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

public class RentVsBuyFragment extends Fragment implements RentvsBuyContract.View {

    private final RentVsBuyPresenter presenter;

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

        Button rentvsBuy = view.findViewById(R.id.btnCalculate);
        rentvsBuy.setOnClickListener(view1 ->
                {
                    HouseBuyDetails houseValueDetails = getHouseValueDetails(view);
                    RentParameters rentParameters = getRentParameters(view);
                    TaxDetails taxDetails = getInvestorDetails(view);
                    BigDecimal inflationRate = getReInvestmentDetails(view);
                    if(houseValueDetails==null || rentParameters==null || taxDetails ==null){
                        showErrorMessage();
                        return;
                    }
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
        return avgInvestmentReturn;
    }


    private TaxDetails getInvestorDetails(@NotNull View view) {
        EditText txtTaxRate = view.findViewById(R.id.txtTaxRate);

        String taxRateString = txtTaxRate.getText().toString();

        if(StringUtils.isNullOrEmpty(taxRateString)){
            return null;
        }

        BigDecimal taxRate = new BigDecimal(taxRateString);

        return new TaxDetails(taxRate);
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

        return new RentParameters(rent,rentAppreciation);
    }

    private HouseBuyDetails getHouseValueDetails(@NotNull View view) {
        EditText txtDownPayment = view.findViewById(R.id.txtDownPayment);
        EditText txtLoanPrincipal = view.findViewById(R.id.txtLoanPrincipal);
        EditText txtLoanTerm = view.findViewById(R.id.txtLoanTerm);
        EditText txtLoanInterest = view.findViewById(R.id.txtLoanInterest);
        EditText houseAppreciation = view.findViewById(R.id.txtPropertyAppreciation);

        String downPaymentString = txtDownPayment.getText().toString();
        String principalString = txtLoanPrincipal.getText().toString();
        String interestString = txtLoanInterest.getText().toString();
        String loanTermString = txtLoanTerm.getText().toString();
        String appreciationString = houseAppreciation.getText().toString();

        if(StringUtils.isNullOrEmpty(downPaymentString) || StringUtils.isNullOrEmpty(principalString)
            || StringUtils.isNullOrEmpty(interestString) || StringUtils.isNullOrEmpty(loanTermString)
            || StringUtils.isNullOrEmpty(appreciationString)){
            return null;
        }

        BigDecimal downPayment = new BigDecimal(downPaymentString);
        BigDecimal principal = new BigDecimal(principalString);
        BigDecimal interest = new BigDecimal(interestString);
        int term = Integer.parseInt(loanTermString);
        BigDecimal appreciation = new BigDecimal(appreciationString);

        LoanDetails loan = new LoanDetails(principal, term, interest);
        return new HouseBuyDetails(downPayment, loan, appreciation);
    }

    @Override
    public void showResult(RentVsBuyResult result) {
        final NavController navController = NavHostFragment.findNavController(this);
        final NavDirections action = RentVsBuyFragmentDirections.actionRentVsBuyFragmentToBuyVsRentResultFragment(result);
        navController.navigate(action);
    }
}