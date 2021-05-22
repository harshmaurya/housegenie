package com.vanguard.housegenie.fragments;

import android.os.Bundle;
import android.view.Gravity;
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
import com.vanguard.housegenie.domain.HouseValueResult;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.vanguard.housegenie.utils.FinUtils.convertToCurrencyFormat;
import static com.vanguard.housegenie.utils.FinUtils.convertToRateFormat;

public class HouseValueResultFragment extends Fragment {
    private HouseValueResult result;

    public HouseValueResultFragment() {
        // Required empty public constructor
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            result = HouseValueResultFragmentArgs.fromBundle(args).getHouseValueResultArgs();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_house_value_result, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {

        Button btnHouseDetails = view.findViewById(R.id.btnShowHouseDetails);
        Button calculateAgain = view.findViewById(R.id.btnCalculateAgain);
        Button home = view.findViewById(R.id.btnHome);

        TextView txtHouseReturns = view.findViewById(R.id.txtHouseReturns);
        TextView txtHouseXirr = view.findViewById(R.id.txtHouseXirr);


        BigDecimal initialInvestment = result.getInvestedAmount();
        double years = round((double)result.getTermInMonths()/12, 1);
        BigDecimal valueFromHouse = result.getReturnFromHouse();
        BigDecimal houseXirr = result.getHouseXirr();
        txtHouseReturns.setText(convertToCurrencyFormat(valueFromHouse));
        txtHouseXirr.setText(convertToRateFormat(houseXirr));

        btnHouseDetails.setOnClickListener((o)->{
            ShowHouseDetailsPopup(view, years, initialInvestment, valueFromHouse, houseXirr);
        });


        calculateAgain.setOnClickListener((o)->{
            final NavController navController = NavHostFragment.findNavController(this);
            navController.popBackStack();
        });

        home.setOnClickListener((o)->{
            final NavController navController = NavHostFragment.findNavController(this);
            final NavDirections action = HouseValueResultFragmentDirections.actionHouseValueResultFragmentToHomeFragment();
            navController.navigate(action);
        });

        Button disclaimer = view.findViewById(R.id.btnDisclaimer);
        disclaimer.setOnClickListener(view1 -> {
            FragmentHelper.showDisclaimerPopup(view, getActivity());
        });

    }


    private void ShowHouseDetailsPopup(@NotNull View view, double term,
                                       BigDecimal investmentInitial, BigDecimal investmentValue, BigDecimal xirr) {

        View popupView = showPopup(view);

        setPopupData(term, investmentInitial, investmentValue, xirr, popupView);

        TextView txtTerm = popupView.findViewById(R.id.txt_popup_description);
        txtTerm.setVisibility(View.VISIBLE);
    }

    private void setPopupData(double term, BigDecimal investmentInitial, BigDecimal investmentValue, BigDecimal xirr, View popupView) {
        TextView txtTerm = popupView.findViewById(R.id.txt_popup_term);
        txtTerm.setText(String.valueOf(term));

        TextView txtInvestmentInitial = popupView.findViewById(R.id.txt_popup_initial_investment);
        txtInvestmentInitial.setText(convertToCurrencyFormat(investmentInitial));

        TextView txtInvestmentValue = popupView.findViewById(R.id.txt_popup_final_investment);
        txtInvestmentValue.setText(convertToCurrencyFormat(investmentValue));

        TextView txtXirr = popupView.findViewById(R.id.txt_popup_xirr);
        txtXirr.setText(convertToRateFormat(xirr));
    }

    @NotNull
    private View showPopup(@NotNull View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                Objects.requireNonNull(getActivity()).getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_investment, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });

        Button dismissButton = popupView.findViewById(R.id.btnDismiss);
        dismissButton.setOnClickListener(v -> popupWindow.dismiss());
        return popupView;
    }

}
