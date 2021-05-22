package com.vanguard.housegenie.fragments;

import android.content.Context;
import android.os.LocaleList;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import com.vanguard.housegenie.R;
import com.vanguard.housegenie.domain.TaxDetails;
import com.vanguard.housegenie.utils.AndroidUtils;
import com.vanguard.housegenie.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import static com.vanguard.housegenie.analytics.FinancialCalculator.toFractionalRate;

interface TaxDetailsFetcher{
    TaxDetails getTaxDetails();
}

public class TaxDetailFragment {

    public static TaxDetailsFetcher SetupTaxView(@NotNull View view, Context context){

       RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
       FrameLayout taxSection = view.findViewById(R.id.taxDetails);

       setupTaxDetails(view, context, taxSection, radioGroup.getCheckedRadioButtonId());
       radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
           setupTaxDetails(view, context, taxSection, checkedId);
       });

       return () -> {
           EditText txtTaxRate = view.findViewById(R.id.txtTaxRate);
           String taxRateString = txtTaxRate.getText().toString();

           EditText txtInterestCap = view.findViewById(R.id.txtInterestCap);
           String interestCapString = txtInterestCap.getText().toString();

           if(StringUtils.isNullOrEmpty(taxRateString) || StringUtils.isNullOrEmpty(interestCapString)){
               return new TaxDetails(BigDecimal.ZERO, BigDecimal.ZERO);
           }

           BigDecimal taxRate = new BigDecimal(taxRateString);
           BigDecimal interestCap = new BigDecimal(interestCapString);

           return new TaxDetails(toFractionalRate(taxRate), interestCap);
       };
   }

    private static void setupTaxDetails(@NotNull View view, Context context, FrameLayout taxSection, int checkedId) {
        if(checkedId == R.id.radioTaxEnable) {
            taxSection.setVisibility(View.VISIBLE);
            setupDefaults(view, context);
        }
        else {
            taxSection.setVisibility(View.GONE);
            setupNoTax(view);
        }
    }

    private static void setupDefaults(@NotNull View view, Context context){
        String country = AndroidUtils.getCountryCode(context);
        if(country.equals("IN")){
            EditText txtTaxRate = view.findViewById(R.id.txtTaxRate);
            txtTaxRate.setText("30");
            EditText txtInterestCap = view.findViewById(R.id.txtInterestCap);
            txtInterestCap.setText("200000");
        }
   }

    private static void setupNoTax(@NotNull View view){
            EditText txtTaxRate = view.findViewById(R.id.txtTaxRate);
            txtTaxRate.setText("0");
            EditText txtInterestCap = view.findViewById(R.id.txtInterestCap);
            txtInterestCap.setText("0");
    }
}
