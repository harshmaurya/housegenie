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
import com.vanguard.housegenie.domain.HouseVsOtherInvestmentResult;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.vanguard.housegenie.utils.FinUtils.convertToCurrencyFormat;
import static com.vanguard.housegenie.utils.FinUtils.convertToRateFormat;

public class HouseVsInvestmentResultFragment extends Fragment {

    private HouseVsOtherInvestmentResult result;

    public HouseVsInvestmentResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            result = HouseVsInvestmentResultFragmentArgs.fromBundle(args).getHousevsInvestmentArgs();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_house_vs_investment_result, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {

        Button btnHouseDetails = view.findViewById(R.id.btnShowHouseDetails);
        Button btnOtherDetails = view.findViewById(R.id.btnShowOtherDetails);
        Button calculateAgain = view.findViewById(R.id.btnCalculateAgain);
        Button home = view.findViewById(R.id.btnHome);

        TextView txtHouseMoreProfitable = view.findViewById(R.id.txt_house_more_profitable);
        TextView txtRentMoreProfitable = view.findViewById(R.id.txt_other_more_profitable);
        ImageView houseWinImage = view.findViewById(R.id.scale_house_win_image);
        ImageView otherWinImage = view.findViewById(R.id.scale_other_win_image);
        TextView txtHouseReturns = view.findViewById(R.id.txtHouseReturns);
        TextView txtOtherReturns = view.findViewById(R.id.txtOtherReturn);
        TextView txtHouseXirr = view.findViewById(R.id.txtHouseXirr);
        TextView txtOtherXirr = view.findViewById(R.id.txtOtherXirr);


        BigDecimal initialInvestment = result.getInvestedAmount();
        int term = result.getTerm();


        // House
        BigDecimal valueFromHouse = result.getReturnFromHouse();
        BigDecimal houseXirr = result.getHouseXirr();
        txtHouseReturns.setText(convertToCurrencyFormat(valueFromHouse));
        txtHouseXirr.setText(convertToRateFormat(houseXirr));


        //Other Investment
        BigDecimal valueFromInvestment = result.getReturnFromInvestment();
        BigDecimal investmentXirr = result.getInvestmentXirr();
        txtOtherReturns.setText(convertToCurrencyFormat(valueFromInvestment));
        txtOtherXirr.setText(convertToRateFormat(investmentXirr));

        if(valueFromHouse.compareTo(valueFromInvestment) > 0){
            txtHouseMoreProfitable.setVisibility(View.VISIBLE);
            houseWinImage.setVisibility(View.VISIBLE);
        }
        else {
            txtRentMoreProfitable.setVisibility(View.VISIBLE);
            otherWinImage.setVisibility(View.VISIBLE);
        }

        btnHouseDetails.setOnClickListener((o)->{
            ShowHouseDetailsPopup(view, term, initialInvestment, valueFromHouse, houseXirr);
        });

        btnOtherDetails.setOnClickListener((o)->{
            ShowOtherDetailsPopup(view, term, initialInvestment, valueFromInvestment, investmentXirr);
        });

        calculateAgain.setOnClickListener((o)->{
            final NavController navController = NavHostFragment.findNavController(this);
            navController.popBackStack();
        });

        home.setOnClickListener((o)->{
            final NavController navController = NavHostFragment.findNavController(this);
            final NavDirections action = HouseVsInvestmentResultFragmentDirections.actionHouseVsInvestmentResultFragmentToHomeFragment();
            navController.navigate(action);
        });

        FragmentHelper.SetupFooterLinks(view, getActivity());
    }

    private void ShowOtherDetailsPopup(@NotNull View view, int term,
                                       BigDecimal investmentInitial, BigDecimal investmentValue, BigDecimal xirr) {
        View popupView = showPopup(view);

        setPopupData(term, investmentInitial, investmentValue, xirr, popupView);

        TextView txtTerm = popupView.findViewById(R.id.txt_popup_description_simple);
        txtTerm.setVisibility(View.VISIBLE);
    }


    private void ShowHouseDetailsPopup(@NotNull View view, int term,
                                       BigDecimal investmentInitial, BigDecimal investmentValue, BigDecimal xirr) {

        View popupView = showPopup(view);

        setPopupData(term, investmentInitial, investmentValue, xirr, popupView);

        TextView txtTerm = popupView.findViewById(R.id.txt_popup_description);
        txtTerm.setVisibility(View.VISIBLE);
    }

    private void setPopupData(int term, BigDecimal investmentInitial, BigDecimal investmentValue, BigDecimal xirr, View popupView) {
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
                requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_investment, null);
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
        return popupView;
    }

}
