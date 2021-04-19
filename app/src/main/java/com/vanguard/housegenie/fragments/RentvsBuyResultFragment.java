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
import com.vanguard.housegenie.analytics.HouseFutureValue;
import com.vanguard.housegenie.domain.RentVsBuyResult;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.vanguard.housegenie.utils.FinUtils.convertToCurrencyFormat;

public class RentvsBuyResultFragment extends Fragment {

    private RentVsBuyResult result;

    public RentvsBuyResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            result = RentvsBuyResultFragmentArgs.fromBundle(args).getRentVsBuyResultArgs();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rent_vs_buy_result, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {

        Button buyBreakup = view.findViewById(R.id.btnShowBuyBreakup);
        Button rentBreakup = view.findViewById(R.id.btnShowRentBreakup);
        Button calculateAgain = view.findViewById(R.id.btnCalculateAgain);
        Button home = view.findViewById(R.id.btnHome);

        TextView txtBuyMoreProfitable = view.findViewById(R.id.txt_buy_more_profitable);
        TextView txtRentMoreProfitable = view.findViewById(R.id.txt_rent_more_profitable);
        TextView txtBuyCost = view.findViewById(R.id.txtBuyCost);
        TextView txtRentCost = view.findViewById(R.id.txtRentCost);
        ImageView buyWinImage = view.findViewById(R.id.scale_buy_win_image);
        ImageView rentWinImage = view.findViewById(R.id.scale_rent_win_image);

        HouseFutureValue houseValue = result.getHouseFutureValue();
        BigDecimal costOfRenting = result.getRentValue();

        //txtRentCost.addTextChangedListener(new MoneyTextWatcher(txtRentCost));
        txtRentCost.setText(convertToCurrencyFormat(costOfRenting));

        BigDecimal emiCost = houseValue.getCostOfEmi();
        BigDecimal downPaymentCost = houseValue.getCostOfDownPayment();
        BigDecimal futureHouseValue = houseValue.getHouseValue();
        BigDecimal costOfBuying = emiCost.add(downPaymentCost).subtract(futureHouseValue);

        txtBuyCost.setText(convertToCurrencyFormat(costOfBuying));

        if(costOfBuying.compareTo(costOfRenting) < 0){
            txtBuyMoreProfitable.setVisibility(View.VISIBLE);
            buyWinImage.setVisibility(View.VISIBLE);
        }
        else {
            txtRentMoreProfitable.setVisibility(View.VISIBLE);
            rentWinImage.setVisibility(View.VISIBLE);
        }

        buyBreakup.setOnClickListener((o)->{
            ShowBuyDetailsPopup(view, result.getTerm(), futureHouseValue, emiCost, downPaymentCost, costOfBuying);
        });

        rentBreakup.setOnClickListener((o)->{
            ShowRentDetailsPopup(view, result.getTerm(), costOfRenting);
        });

        calculateAgain.setOnClickListener((o)->{
            final NavController navController = NavHostFragment.findNavController(this);
            navController.popBackStack();
        });

        home.setOnClickListener((o)->{
            final NavController navController = NavHostFragment.findNavController(this);
            final NavDirections action = RentvsBuyResultFragmentDirections.actionBuyVsRentResultFragmentToHomeFragment();
            navController.navigate(action);
        });

    }

    private void ShowRentDetailsPopup(@NotNull View view, int term,
                                     BigDecimal rentCost) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                Objects.requireNonNull(getActivity()).getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_rent, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });

        Button dismissButton = popupView.findViewById(R.id.btnDismiss);
        dismissButton.setOnClickListener(v -> popupWindow.dismiss());

        TextView txtTerm = popupView.findViewById(R.id.txt_popup_term);
        txtTerm.setText(String.valueOf(term));

        TextView txtEmi = popupView.findViewById(R.id.txt_popup_rent_value);
        txtEmi.setText(convertToCurrencyFormat(rentCost));
    }


    private void ShowBuyDetailsPopup(@NotNull View view, int term,
                                     BigDecimal houseValue, BigDecimal costOfEmi, BigDecimal costOfDownPayment, BigDecimal netCost) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                Objects.requireNonNull(getActivity()).getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_buy, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });

        Button dismissButton = popupView.findViewById(R.id.btnDismiss);
        dismissButton.setOnClickListener(v -> popupWindow.dismiss());

        TextView txtTerm = popupView.findViewById(R.id.txt_popup_term);
        txtTerm.setText(String.valueOf(term));

        TextView txtEmi = popupView.findViewById(R.id.txt_popup_emi_cost);
        txtEmi.setText(convertToCurrencyFormat(costOfEmi));

        TextView txtDownPayment = popupView.findViewById(R.id.txt_popup_downpayment_cost);
        txtDownPayment.setText(convertToCurrencyFormat(costOfDownPayment));

        TextView txtHouseValue = popupView.findViewById(R.id.txt_popup_house_value);
        txtHouseValue.setText(convertToCurrencyFormat(houseValue));

        TextView txtNetCost = popupView.findViewById(R.id.txt_popup_net_cost_own);
        txtNetCost.setText(convertToCurrencyFormat(netCost));
    }
}