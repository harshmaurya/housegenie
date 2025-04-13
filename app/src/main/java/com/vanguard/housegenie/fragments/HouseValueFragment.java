package com.vanguard.housegenie.fragments;

import android.app.DatePickerDialog;
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
import com.vanguard.housegenie.R;
import com.vanguard.housegenie.analytics.FinancialCalculator;
import com.vanguard.housegenie.contracts.HouseValueContract;
import com.vanguard.housegenie.domain.*;
import com.vanguard.housegenie.presenters.HouseValuePresenter;
import com.vanguard.housegenie.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;

import static com.vanguard.housegenie.analytics.FinancialCalculator.toFractionalRate;

public class HouseValueFragment extends Fragment implements HouseValueContract.View {

    private final HouseValuePresenter presenter;
    final Calendar myCalendar = Calendar.getInstance();
    private final String dateFormat = "dd/MM/yyyy";

    public HouseValueFragment() {
        presenter = new HouseValuePresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_house_value, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {

        TaxDetailsFetcher taxDetailsFetcher = TaxDetailFragment.SetupTaxView(view, getContext());
        PurchaseDetailsFetcher purchaseDetailsFetcher = PurchaseDetailsFragment.SetupPurchaseView(view, getContext());
        EditText txtStartDate= view.findViewById(R.id.txtStartDate);
        DatePickerDialog.OnDateSetListener date = (view12, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            txtStartDate.setText(sdf.format(myCalendar.getTime()));
        };

        txtStartDate.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        Button calculate = view.findViewById(R.id.btnCalculate);

        calculate.setOnClickListener(view1 ->
        {
            String startDateString = txtStartDate.getText().toString();
            LocalDate startDate = LocalDate.now();
            try {
                startDate = LocalDate.parse(startDateString, DateTimeFormatter.ofPattern(dateFormat));
            }
            catch (Exception ex){
                showErrorMessage("Please check if date is set correctly.");
            }

            PurchaseDetails purchaseDetails = purchaseDetailsFetcher.getPurchaseDetails();
            RentParameters rentParameters = getRentParameters(view);
            ITaxDetails taxDetails = taxDetailsFetcher.getTaxDetails();
            if(purchaseDetails==null || rentParameters==null || taxDetails ==null){
                showErrorMessage();
                return;
            }
            BigDecimal inflationRate = getInflationRate(view);
            HouseBuyDetails houseValueDetails = new HouseBuyDetails(purchaseDetails.getDownPayment(),
                    purchaseDetails.getLoanDetails(), getHouseAppreciation(view, startDate, purchaseDetails), purchaseDetails.getMaintenance());
            HouseValueArgs args = new HouseValueArgs(houseValueDetails, rentParameters, taxDetails, startDate, inflationRate);
            presenter.calculate(args);
        });
    }

    private BigDecimal getInflationRate(@NotNull View view){

        EditText txtAvgInvestmentReturn = view.findViewById(R.id.txtInflationRate);
        String avgInvestmentRateString = txtAvgInvestmentReturn.getText().toString();
        if(StringUtils.isNullOrEmpty(avgInvestmentRateString)){
            return null;
        }
        BigDecimal avgInvestmentReturn = new BigDecimal(avgInvestmentRateString);
        return toFractionalRate(avgInvestmentReturn);
    }

    private void showErrorMessage() {
        Toast.makeText(this.getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String msg) {
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
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

    private BigDecimal getHouseAppreciation(@NotNull View view, LocalDate startDate, PurchaseDetails buyDetails) {

        EditText houseCurrentValue = view.findViewById(R.id.txtPropertyCurrentValue);
        String currentValueString = houseCurrentValue.getText().toString();
        BigDecimal downPayment = buyDetails.getDownPayment();
        BigDecimal principal = buyDetails.getLoanDetails().getPrincipal();
        BigDecimal purchaseValue = downPayment.add(principal);
        BigDecimal currentValue = new BigDecimal(currentValueString);
        long monthsPassed = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), LocalDate.now().withDayOfMonth(1));
        double years = (double) monthsPassed / 12;
        return FinancialCalculator.getRate(purchaseValue, currentValue, years);
    }

    @Override
    public void showResult(HouseValueResult result) {
        final NavController navController = NavHostFragment.findNavController(this);
        final NavDirections action = HouseValueFragmentDirections.actionHouseValueFragmentToHouseValueResultFragment(result);
        navController.navigate(action);
    }
}